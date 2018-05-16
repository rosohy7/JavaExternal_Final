package external.letiuka.persistence.dal.dao;

import external.letiuka.mvc.model.TransactionType;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.FromTransactionEntity;
import external.letiuka.persistence.entities.PaymentTransactionEntity;
import external.letiuka.persistence.entities.ToTransactionEntity;
import external.letiuka.persistence.entities.TransactionEntity;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;

public class DefaultTransactionDAO implements TransactionDAO {
    private final TransactionManager manager;
    private static final Logger logger = Logger.getLogger(DefaultTransactionDAO.class);

    public DefaultTransactionDAO(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public void insertTransaction(TransactionEntity transaction) throws DAOException {
        String sqlBase = "INSERT INTO `transaction`(`time_stamp`,`balance_change`,`bank_fee`," +
                "`transacction_type`,`bank_account_id`,`pair_transaction_id`) VALUES(?,?,?,?,?,?)";
        Connection cn;
        PreparedStatement ps;
        ResultSet generated;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, transaction.getTimeStamp());
            ps.setDouble(2, transaction.getBalanceChange());
            ps.setDouble(3, transaction.getBankFee());
            ps.setString(4, transaction.getType().toString());
            ps.setLong(5, transaction.getAccountId());
            ps.setObject(6, transaction.getPairId(), Types.BIGINT);
            ps.executeUpdate();
            generated = ps.getGeneratedKeys();
            if (generated.next())
                transaction.setId(generated.getLong(1));
            else return;
            switch (transaction.getType()) {
                case TRANSFER_TO:
                    ToTransactionEntity toTrans = (ToTransactionEntity) transaction;
                    String sqlTo = "INSERT INTO `transfer_to_transaction`" +
                            "(`transaction_id`,`sender_account_number`) VALUES(?,?)";
                    ps = cn.prepareStatement(sqlTo);
                    ps.setLong(1, toTrans.getId());
                    ps.setString(2, toTrans.getSenderNumber());
                    ps.executeUpdate();
                    break;
                case TRANSFER_FROM:
                    FromTransactionEntity fromTrans = (FromTransactionEntity) transaction;
                    String sqlFrom = "INSERT INTO `transfer_from_transaction`" +
                            "(`transaction_id`,`receiver_account_number`) VALUES(?,?)";
                    ps = cn.prepareStatement(sqlFrom);
                    ps.setLong(1, fromTrans.getId());
                    ps.setString(2, fromTrans.getReceiverNumber());
                    ps.executeUpdate();
                    break;
                case PAYMENT:
                    PaymentTransactionEntity paymentTrans = (PaymentTransactionEntity) transaction;
                    String sqlPayment = "INSERT INTO `payment_transaction`" +
                            "(`transaction_id`,`payment_number`) VALUES(?,?)";
                    ps = cn.prepareStatement(sqlPayment);
                    ps.setLong(1, paymentTrans.getId());
                    ps.setString(2, paymentTrans.getPaymentNumber());
                    ps.executeUpdate();
                    break;
                default:
                    success=false;
                    String message = "The DAO does not support the entity subtype";
                    logger.log(Level.ERROR, message);
                    throw new DAOException(message);
            }
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to add entity to database");
            success = false;
            throw new DAOException(e);
        } finally {
            try {
                manager.returnConnection(success);       // If not in a transaction then commit or rollback
            } catch (Exception e) {
                if (success) throw new DAOException(e);  // Do not override original exception if any
            }
        }
    }

    @Override
    public TransactionEntity readTransaction(long id) throws DAOException {
        String sql = "SELECT * FROM `transaction` WHERE `transaction_id` = ?";
        Connection cn;
        PreparedStatement ps;
        ResultSet rs, rsLeaf;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                TransactionEntity baseTransaction;
                TransactionType type = TransactionType.valueOf(rs.getString("account_type"));
                switch (type) {
                    case TRANSFER_TO:
                        ToTransactionEntity toTransaction = new ToTransactionEntity();
                        baseTransaction=toTransaction;
                        String sqlTo = "SELECT * FROM `transfer_to_transaction`" +
                                " WHERE `transaction_id` = ?";
                        ps = cn.prepareStatement(sqlTo);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            toTransaction.setSenderNumber(rsLeaf.getString("sender_account_number"));
                        } else {
                            logger.log(Level.ERROR, "No matching entity subtype for base entity");
                            return null;
                        }
                        break;
                    case TRANSFER_FROM:
                        FromTransactionEntity fromTransaction = new FromTransactionEntity();
                        baseTransaction=fromTransaction;
                        String sqlFrom = "SELECT * FROM `transfer_from_transaction`" +
                                " WHERE `transaction_id` = ?";
                        ps = cn.prepareStatement(sqlFrom);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            fromTransaction.setReceiverNumber(rsLeaf.getString("receiver_account_number"));
                        } else {
                            logger.log(Level.ERROR, "No matching entity subtype for base entity");
                            return null;
                        }
                        break;
                    case PAYMENT:
                        PaymentTransactionEntity paymentTransaction = new PaymentTransactionEntity();
                        baseTransaction=paymentTransaction;
                        String sqlPayment = "SELECT * FROM `payment_transaction`" +
                                " WHERE `transaction_id` = ?";
                        ps = cn.prepareStatement(sqlPayment);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            paymentTransaction.setPaymentNumber(rsLeaf.getString("payment_number"));
                        } else {
                            logger.log(Level.ERROR, "No matching entity subtype for base entity");
                            return null;
                        }
                        break;
                    default:
                        success=false;
                        String message = "The DAO does not support the entity subtype";
                        logger.log(Level.ERROR, message);
                        throw new DAOException(message);
                }
                baseTransaction.setId(id);
                baseTransaction.setType(type);
                baseTransaction.setTimeStamp(rs.getTimestamp("time_stamp"));
                baseTransaction.setBalanceChange(rs.getDouble("balance_change"));
                baseTransaction.setBankFee(rs.getDouble("bank_fee"));
                baseTransaction.setAccountId(rs.getLong("bank_account_id"));
                baseTransaction.setPairId(rs.getObject("pair_transaction_id",Long.class));;
                return baseTransaction;
            } else {
                return null;
            }
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to read entity from database");
            success = false;
            throw new DAOException(e);
        } finally {
            try {
                manager.returnConnection(success);       // If not in a transaction then commit or rollback
            } catch (Exception e) {
                if (success) throw new DAOException(e);  // Do not override original exception if any
            }
        }
    }

    @Override
    public void deleteTransaction(long id) throws DAOException {
        String sqlBase = "DELETE FROM `transaction` WHERE `transaction_id` = ?";
        String sqlTo = "DELETE FROM `transfer_to_transaction` WHERE `transaction_id` = ?";
        String sqlFrom = "DELETE FROM `transfer_from_transaction` WHERE `transaction_id` = ?";
        String sqlPayment = "DELETE FROM `payment_transaction` WHERE `transaction_id` = ?";
        Connection cn;
        PreparedStatement ps;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sqlBase);
            ps.setLong(1, id);
            ps.executeUpdate();

            ps = cn.prepareStatement(sqlTo);
            ps.setLong(1, id);
            ps.executeUpdate();

            ps = cn.prepareStatement(sqlFrom);
            ps.setLong(1, id);
            ps.executeUpdate();

            ps = cn.prepareStatement(sqlPayment);
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to add entity to database");
            success = false;
            throw new DAOException(e);
        } finally {
            try {
                manager.returnConnection(success);       // If not in a transaction then commit or rollback
            } catch (Exception e) {
                if (success) throw new DAOException(e);  // Do not override original exception if any
            }
        }
    }
}

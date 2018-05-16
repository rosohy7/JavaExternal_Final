package external.letiuka.persistence.dal.dao;

import external.letiuka.mvc.model.AccountType;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.BankAccountEntity;
import external.letiuka.persistence.entities.CreditBankAccountEntity;
import external.letiuka.persistence.entities.DepositBankAccountEntity;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultBankAccountDAO implements BankAccountDAO {
    private final TransactionManager manager;
    private static final Logger logger = Logger.getLogger(DefaultBankAccountDAO.class);

    public DefaultBankAccountDAO(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public void insertAccount(BankAccountEntity account) throws DAOException {
        String sqlBase = "INSERT INTO `bank_account`(`expires`,`account_number`,`account_type`," +
                "`balance`,`confirmed`,`latest_update`,`user_id`) VALUES(?,?,?,?,?,?,?)";
        Connection cn;
        PreparedStatement ps;
        ResultSet generated;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, account.getExpires());
            ps.setString(2, account.getAccountNumber());
            ps.setString(3, account.getType().toString());
            ps.setDouble(4, account.getAccountBalance());
            ps.setBoolean(5, account.isConfirmed());
            ps.setDate(6, account.getLatestUpdate());
            ps.setLong(7, account.getUserId());
            ps.executeUpdate();
            generated = ps.getGeneratedKeys();
            if (generated.next())
                account.setId(generated.getLong(1));
            else return;
            switch (account.getType()) {
                case CREDIT:
                    CreditBankAccountEntity credAccount = (CreditBankAccountEntity) account;
                    String sqlCredit = "INSERT INTO `credit_bank_account`" +
                            "(`bank_account_id`,`credit_limit`,`interest_rate`,`accrued_interest`) VALUES(?,?,?,?)";
                    ps = cn.prepareStatement(sqlCredit);
                    ps.setLong(1, credAccount.getId());
                    ps.setDouble(2, credAccount.getCreditLimit());
                    ps.setDouble(3, credAccount.getInterestRate());
                    ps.setDouble(4, credAccount.getAccruedInterest());
                    ps.executeUpdate();
                    break;
                case DEPOSIT:
                    DepositBankAccountEntity depAccount = (DepositBankAccountEntity) account;
                    String sqlDeposit = "INSERT INTO `deposit_bank_account`" +
                            "(`bank_account_id`,`interest_rate`,`accrued_interest`) VALUES(?,?,?)";
                    ps = cn.prepareStatement(sqlDeposit);
                    ps.setLong(1, depAccount.getId());
                    ps.setDouble(2, depAccount.getInterestRate());
                    ps.setDouble(3, depAccount.getAccruedInterest());
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
    public BankAccountEntity readAccount(long id) throws DAOException {
        String sql = "SELECT * FROM `bank_account` WHERE `bank_account_id` = ?";
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
                BankAccountEntity baseAccount;
                AccountType type = AccountType.valueOf(rs.getString("account_type"));
                switch (type) {
                    case CREDIT:
                        baseAccount = new CreditBankAccountEntity();
                        CreditBankAccountEntity credAccount = (CreditBankAccountEntity) baseAccount;
                        String sqlCredit = "SELECT * FROM `credit_bank_account`" +
                                " WHERE `bank_account_id` = ?";
                        ps = cn.prepareStatement(sqlCredit);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            credAccount.setCreditLimit(rsLeaf.getDouble("credit_limit"));
                            credAccount.setInterestRate(rsLeaf.getDouble("interest_rate"));
                            credAccount.setAccruedInterest(rsLeaf.getDouble("accrued_interest"));
                        } else {
                            logger.log(Level.ERROR, "No matching entity subtype for base entity");
                            return null;
                        }
                        break;
                    case DEPOSIT:
                        baseAccount = new DepositBankAccountEntity();
                        DepositBankAccountEntity depAccount = (DepositBankAccountEntity) baseAccount;
                        String sqlDeposit = "SELECT * FROM `deposit_bank_account`" +
                                " WHERE `bank_account_id` = ?";
                        ps = cn.prepareStatement(sqlDeposit);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            depAccount.setInterestRate(rsLeaf.getDouble("interest_rate"));
                            depAccount.setAccruedInterest(rsLeaf.getDouble("accrued_interest"));
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
                baseAccount.setId(id);
                baseAccount.setType(type);
                baseAccount.setExpires(rs.getDate("expires"));
                baseAccount.setAccountNumber(rs.getString("account_number"));
                baseAccount.setAccountBalance(rs.getDouble("balance"));
                baseAccount.setConfirmed(rs.getBoolean("confirmed"));
                baseAccount.setLatestUpdate(rs.getDate("latest_update"));
                baseAccount.setUserId(rs.getLong("user_id"));
                return baseAccount;
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
    public BankAccountEntity readAccount(String accountNumber) throws DAOException {
        String sql = "SELECT * FROM `bank_account` WHERE `account_number` = ?";
        Connection cn;
        PreparedStatement ps;
        ResultSet rs, rsLeaf;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, accountNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                BankAccountEntity baseAccount;
                AccountType type = AccountType.valueOf(rs.getString("account_type"));
                Long id = rs.getLong("bank_account_id");
                switch (type) {
                    case CREDIT:
                        baseAccount = new CreditBankAccountEntity();
                        CreditBankAccountEntity credAccount = (CreditBankAccountEntity) baseAccount;
                        String sqlCredit = "SELECT * FROM `credit_bank_account`" +
                                " WHERE `bank_account_id` = ?";
                        ps = cn.prepareStatement(sqlCredit);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            credAccount.setCreditLimit(rsLeaf.getDouble("credit_limit"));
                            credAccount.setInterestRate(rsLeaf.getDouble("interest_rate"));
                            credAccount.setAccruedInterest(rsLeaf.getDouble("accrued_interest"));
                        } else {
                            logger.log(Level.ERROR, "No matching entity subtype for base entity");
                            return null;
                        }
                        break;
                    case DEPOSIT:
                        baseAccount = new DepositBankAccountEntity();
                        DepositBankAccountEntity depAccount = (DepositBankAccountEntity) baseAccount;
                        String sqlDeposit = "SELECT * FROM `deposit_bank_account`" +
                                " WHERE `bank_account_id` = ?";
                        ps = cn.prepareStatement(sqlDeposit);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            depAccount.setInterestRate(rsLeaf.getDouble("interest_rate"));
                            depAccount.setAccruedInterest(rsLeaf.getDouble("accrued_interest"));
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
                baseAccount.setId(id);
                baseAccount.setType(type);
                baseAccount.setExpires(rs.getDate("expires"));
                baseAccount.setAccountNumber(accountNumber);
                baseAccount.setAccountBalance(rs.getDouble("balance"));
                baseAccount.setConfirmed(rs.getBoolean("confirmed"));
                baseAccount.setLatestUpdate(rs.getDate("latest_update"));
                baseAccount.setUserId(rs.getLong("user_id"));
                return baseAccount;
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
    public List<BankAccountEntity> readUserAccounts(long userId) throws DAOException {
        List<BankAccountEntity> results = new ArrayList<BankAccountEntity>();
        String sql = "SELECT * FROM `bank_account` WHERE `user_id` = ? ORDER BY `bank_account_id` ASC";
        Connection cn;
        PreparedStatement ps;
        ResultSet rs, rsLeaf;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                BankAccountEntity baseAccount;
                AccountType type = AccountType.valueOf(rs.getString("account_type"));
                long id=rs.getLong("bank_account_id");
                switch (type) {
                    case CREDIT:
                        CreditBankAccountEntity credAccount = new CreditBankAccountEntity();
                        baseAccount = credAccount;
                        String sqlCredit = "SELECT * FROM `credit_bank_account`" +
                                " WHERE `bank_account_id` = ?";
                        ps = cn.prepareStatement(sqlCredit);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            credAccount.setCreditLimit(rsLeaf.getDouble("credit_limit"));
                            credAccount.setInterestRate(rsLeaf.getDouble("interest_rate"));
                            credAccount.setAccruedInterest(rsLeaf.getDouble("accrued_interest"));
                        } else {
                            logger.log(Level.ERROR, "No matching entity subtype for base entity");
                            return null;
                        }
                        break;
                    case DEPOSIT:
                        baseAccount = new DepositBankAccountEntity();
                        DepositBankAccountEntity depAccount = (DepositBankAccountEntity) baseAccount;
                        String sqlDeposit = "SELECT * FROM `deposit_bank_account`" +
                                " WHERE `bank_account_id` = ?";
                        ps = cn.prepareStatement(sqlDeposit);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            depAccount.setInterestRate(rsLeaf.getDouble("interest_rate"));
                            depAccount.setAccruedInterest(rsLeaf.getDouble("accrued_interest"));
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
                baseAccount.setId(id);
                baseAccount.setType(type);
                baseAccount.setExpires(rs.getDate("expires"));
                baseAccount.setAccountNumber(rs.getString("account_number"));
                baseAccount.setAccountBalance(rs.getDouble("balance"));
                baseAccount.setConfirmed(rs.getBoolean("confirmed"));
                baseAccount.setLatestUpdate(rs.getDate("latest_update"));
                baseAccount.setUserId(rs.getLong("user_id"));
                results.add(baseAccount);
            }
            return results;
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
    public List<BankAccountEntity> readUnconfirmedAccounts
            (long offset, long count) throws DAOException {
        List<BankAccountEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM `bank_account` WHERE `confirmed` = FALSE" +
                " ORDER BY `bank_account_id` ASC LIMIT ?,?";
        Connection cn;
        PreparedStatement ps;
        ResultSet rs, rsLeaf;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setLong(1, offset);
            ps.setLong(2, count);
            rs = ps.executeQuery();
            while (rs.next()) {
                BankAccountEntity baseAccount;
                AccountType type = AccountType.valueOf(rs.getString("account_type"));
                long id=rs.getLong("bank_account_id");
                switch (type) {
                    case CREDIT:
                        CreditBankAccountEntity credAccount = new CreditBankAccountEntity();
                        baseAccount = credAccount;
                        String sqlCredit = "SELECT * FROM `credit_bank_account`" +
                                " WHERE `bank_account_id` = ?";
                        ps = cn.prepareStatement(sqlCredit);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            credAccount.setCreditLimit(rsLeaf.getDouble("credit_limit"));
                            credAccount.setInterestRate(rsLeaf.getDouble("interest_rate"));
                            credAccount.setAccruedInterest(rsLeaf.getDouble("accrued_interest"));
                        } else {
                            logger.log(Level.ERROR, "No matching entity subtype for base entity");
                            return null;
                        }
                        break;
                    case DEPOSIT:
                        baseAccount = new DepositBankAccountEntity();
                        DepositBankAccountEntity depAccount = (DepositBankAccountEntity) baseAccount;
                        String sqlDeposit = "SELECT * FROM `deposit_bank_account`" +
                                " WHERE `bank_account_id` = ?";
                        ps = cn.prepareStatement(sqlDeposit);
                        ps.setLong(1, id);
                        rsLeaf = ps.executeQuery();
                        if (rsLeaf.next()) {
                            depAccount.setInterestRate(rsLeaf.getDouble("interest_rate"));
                            depAccount.setAccruedInterest(rsLeaf.getDouble("accrued_interest"));
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
                baseAccount.setId(id);
                baseAccount.setType(type);
                baseAccount.setExpires(rs.getDate("expires"));
                baseAccount.setAccountNumber(rs.getString("account_number"));
                baseAccount.setAccountBalance(rs.getDouble("balance"));
                baseAccount.setConfirmed(rs.getBoolean("confirmed"));
                baseAccount.setLatestUpdate(rs.getDate("latest_update"));
                baseAccount.setUserId(rs.getLong("user_id"));
                results.add(baseAccount);
            }
            return results;
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
    public long getUnconfirmedAccountCount() throws DAOException {
        String sql = "SELECT COUNT(*) AS total FROM `bank_account` WHERE `confirmed` = FALSE";
        Connection cn;
        PreparedStatement ps;
        ResultSet rs, rsLeaf;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
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
    public void updateAccount(BankAccountEntity account) throws DAOException {
        String sqlBase = "UPDATE `bank_account` SET `expires` = ?," +
                "`account_number`=?, `balance`=?,`confirmed`=?," +
                "`latest_update`=?,`user_id`=?) WHERE `bank_account_id` = ?";
        Connection cn;
        PreparedStatement ps;
        ResultSet generated;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sqlBase);
            ps.setDate(1, account.getExpires());
            ps.setString(2, account.getAccountNumber());
            ps.setDouble(3, account.getAccountBalance());
            ps.setBoolean(4, account.isConfirmed());
            ps.setDate(5, account.getLatestUpdate());
            ps.setLong(6, account.getUserId());
            ps.setLong(7, account.getId());
            ps.executeUpdate();
            switch (account.getType()) {
                case CREDIT:
                    CreditBankAccountEntity credAccount = (CreditBankAccountEntity) account;
                    String sqlCredit = "UPDATE `credit_bank_account`" +
                            " SET `credit_limit`=?,`interest_rate`=?,`accrued_interest`=?)" +
                            " WHERE `bank_account_id` = ?";
                    ps = cn.prepareStatement(sqlCredit);
                    ps.setDouble(1, credAccount.getCreditLimit());
                    ps.setDouble(2, credAccount.getInterestRate());
                    ps.setDouble(3, credAccount.getAccruedInterest());
                    ps.setLong(4, credAccount.getId());
                    ps.executeUpdate();
                    break;
                case DEPOSIT:
                    DepositBankAccountEntity depAccount = (DepositBankAccountEntity) account;
                    String sqlDeposit = "UPDATE `deposit_bank_account`" +
                            " SET `interest_rate`=?,`accrued_interest`=?)" +
                            " WHERE `bank_account_id` = ?";
                    ps = cn.prepareStatement(sqlDeposit);
                    ps.setDouble(1, depAccount.getInterestRate());
                    ps.setDouble(2, depAccount.getAccruedInterest());
                    ps.setLong(3, depAccount.getId());
                    ps.executeUpdate();
                    break;
                default:
                    success=false;
                    String message = "The DAO does not support the entity subtype";
                    logger.log(Level.ERROR, message);
                    throw new DAOException(message);
            }
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to update entity in database");
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
    public void confirmAccount(String accountNumber, Date expires) throws DAOException {
        String sqlBase = "UPDATE `bank_account` SET `expires` = ?," +
                ",`confirmed`=TRUE" +
                " WHERE `account_number` = ?";
        Connection cn;
        PreparedStatement ps;
        boolean success = true;
        try {
            cn = manager.getConnection();
            ps = cn.prepareStatement(sqlBase);
            ps.setDate(1, expires);
            ps.setString(2,accountNumber);
            ps.executeUpdate();

        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to confirm account");
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
    public void deleteAccount(long id) throws DAOException {
        String sqlGettype = "SELECT `account_type` FROM `bank_account` WHERE `bank_account_id` = ?";
        String sqlBase = "DELETE FROM `bank_account` WHERE `bank_account_id` = ?";
        Connection cn;
        PreparedStatement ps;
        ResultSet rs;
        boolean success = true;
        try {
            AccountType type;
            cn = manager.getConnection();
            ps = cn.prepareStatement(sqlGettype);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                type = AccountType.valueOf(rs.getString("account_type"));
            } else return;
            ps = cn.prepareStatement(sqlBase);
            ps.setLong(1, id);
            ps.executeUpdate();
            switch (type) {
                case CREDIT:
                    String sqlCredit = "DELETE FROM `credit_bank_account`" +
                            " WHERE `bank_account_id` = ?";
                    ps = cn.prepareStatement(sqlCredit);
                    ps.setLong(1, id);
                    ps.executeUpdate();
                    break;
                case DEPOSIT:
                    String sqlDeposit = "SELECT * FROM `deposit_bank_account`" +
                            " WHERE `bank_account_id` = ?";
                    ps = cn.prepareStatement(sqlDeposit);
                    ps.setLong(1, id);
                    ps.executeUpdate();
                    break;
                default:
                    success=false;
                    String message = "The DAO does not support the entity subtype";
                    logger.log(Level.ERROR, message);
                    throw new DAOException(message);
            }
        } catch (SQLException | TransactionException e) {
            logger.log(Level.WARN, "Failed to delete entity from database");
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

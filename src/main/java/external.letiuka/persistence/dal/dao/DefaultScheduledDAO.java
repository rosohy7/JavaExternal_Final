package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DefaultScheduledDAO implements ScheduledDAO {
    private TransactionManager manager;
    private static final Logger logger = Logger.getLogger(DefaultScheduledDAO.class);

    public DefaultScheduledDAO(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public void accumulateInterest() throws DAOException {
        logger.log(Level.DEBUG, "Sending interest accumulation query");
        String sqlCredit = "UPDATE `bank_account` INNER JOIN `credit_bank_account` " +
                " ON `bank_account`.`bank_account_id` = `credit_bank_account`.`bank_account_id` " +
                "    SET `credit_bank_account`.`accrued_interest` = " +
                "    IF(`bank_account`.`balance`>0," +
                "    `credit_bank_account`.`accrued_interest`," +
                "    `credit_bank_account`.`accrued_interest` + " +
                "    `bank_account`.`balance`*`credit_bank_account`.`interest_rate`/(100.0*365))" +
                "WHERE `bank_account`.`bank_account_id` > 0";

        String sqlDeposit = "UPDATE `bank_account` INNER JOIN `deposit_bank_account`\n" +
                " ON `bank_account`.`bank_account_id` = `deposit_bank_account`.`bank_account_id`    \n" +
                "    SET `deposit_bank_account`.`accrued_interest` = \n" +
                "    `deposit_bank_account`.`accrued_interest` + \n" +
                "    `bank_account`.`balance`*`deposit_bank_account`.`interest_rate`/(100.0*365)\n" +
                "WHERE `bank_account`.`bank_account_id` > 0";
        Connection cn;
        Statement st;
        boolean success = true;
        try {
            cn = manager.getConnection();
            st=cn.createStatement();
            st.executeUpdate(sqlCredit);
            st.executeUpdate(sqlDeposit);
        } catch (SQLException | TransactionException e) {
            logger.log(Level.ERROR, "Failed to perform scheduled interest accumulation");
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
    public void flushInterest() throws DAOException {
        String sqlCredit = "UPDATE `bank_account` INNER JOIN `credit_bank_account` " +
                " ON `bank_account`.`bank_account_id` = `credit_bank_account`.`bank_account_id` " +
                "    SET `bank_account`.`balance` =  " +
                "    `bank_account`.`balance`+`credit_bank_account`.`accrued_interest`, " +
                "    `credit_bank_account`.`accrued_interest` = 0 " +
                "WHERE `bank_account`.`bank_account_id` > 0";

        String sqlDeposit = "UPDATE `bank_account` INNER JOIN `deposit_bank_account` " +
                " ON `bank_account`.`bank_account_id` = `deposit_bank_account`.`bank_account_id` " +
                "    SET `bank_account`.`balance` = " +
                "    `bank_account`.`balance`+`deposit_bank_account`.`accrued_interest`, " +
                "    `deposit_bank_account`.`accrued_interest` = 0 " +
                "WHERE `bank_account`.`bank_account_id` > 0";
        Connection cn;
        Statement st;
        boolean success = true;
        try {
            cn = manager.getConnection();
            st=cn.createStatement();
            st.executeUpdate(sqlCredit);
            st.executeUpdate(sqlDeposit);
        } catch (SQLException | TransactionException e) {
            logger.log(Level.ERROR, "Failed to release accumulated interest");
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


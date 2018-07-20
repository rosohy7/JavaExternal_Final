package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class DefaultScheduledDAO implements ScheduledDAO {
    private final SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(DefaultScheduledDAO.class);

    public DefaultScheduledDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void accumulateInterest(){

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
        Session session = sessionFactory.getCurrentSession();
        session.createSQLQuery(sqlCredit).executeUpdate();
        session.createSQLQuery(sqlDeposit).executeUpdate();
    }

    @Override
    public void flushInterest(){
        logger.log(Level.DEBUG, "Sending interest flush query");
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
        Session session = sessionFactory.getCurrentSession();
        session.createSQLQuery(sqlCredit).executeUpdate();
        session.createSQLQuery(sqlDeposit).executeUpdate();
    }
}


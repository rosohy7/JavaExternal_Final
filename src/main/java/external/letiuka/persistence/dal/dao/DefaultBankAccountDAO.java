package external.letiuka.persistence.dal.dao;

import external.letiuka.modelviewcontroller.model.AccountType;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.BankAccountEntity;
import external.letiuka.persistence.entities.CreditBankAccountEntity;
import external.letiuka.persistence.entities.DepositBankAccountEntity;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DefaultBankAccountDAO implements BankAccountDAO {
    private final TransactionManager manager;
    private static final Logger logger = Logger.getLogger(DefaultBankAccountDAO.class);

    public DefaultBankAccountDAO(TransactionManager manager) {
        this.manager = manager;
    }


    @Override
    public BankAccountEntity readAccount(String accountNumber) {
        Session session = manager.getSession();
        try {

            return (BankAccountEntity) session
                    .createQuery("FROM BankAccount account WHERE account.accountNumber = :number")
                    .setParameter("number", accountNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<BankAccountEntity> readUserAccounts(long userId, long offset, long count) {
        Session session = manager.getSession();
        try {
            return session
                    .createQuery("FROM BankAccount account" +
                            " WHERE account.user.id = :user_id ORDER BY account.id")
                    .setMaxResults((int) count)
                    .setFirstResult((int) offset)
                    .setParameter("user_id", userId)
                    .list();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<BankAccountEntity> readUserAccounts(String login, long offset, long count) {
        Session session = manager.getSession();
        return session
                .createQuery("FROM BankAccount account" +
                        " WHERE account.user.login = :login AND account.confirmed = TRUE ORDER BY account.id")
                .setMaxResults((int) count)
                .setFirstResult((int) offset)
                .setParameter("login", login)
                .list();
    }


    @Override
    public List<BankAccountEntity> readUnconfirmedAccounts
            (long offset, long count) {
        Session session = manager.getSession();
        return session
                .createQuery("FROM BankAccount account" +
                        " WHERE account.confirmed = FALSE ORDER BY account.id")
                .setMaxResults((int) count)
                .setFirstResult((int) offset)
                .list();
    }

    @Override
    public long getUnconfirmedAccountCount() {
        Session session = manager.getSession();
        return (Long) session
                .createQuery("SELECT COUNT(*) FROM BankAccount account" +
                        " WHERE account.confirmed = FALSE")
                .getSingleResult();
    }

    @Override
    public long getUserAccountCount(String login) {
        Session session = manager.getSession();
        return (Long) session
                .createQuery("SELECT COUNT(*) FROM BankAccount account" +
                        " WHERE account.confirmed = TRUE AND account.user.login = :login")
                .setParameter("login", login)
                .getSingleResult();

    }


}

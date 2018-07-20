package external.letiuka.persistence.dal.dao;

import external.letiuka.modelviewcontroller.model.TransactionType;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.FromTransactionEntity;
import external.letiuka.persistence.entities.PaymentTransactionEntity;
import external.letiuka.persistence.entities.ToTransactionEntity;
import external.letiuka.persistence.entities.TransactionEntity;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DefaultTransactionDAO implements TransactionDAO {
    private final SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(DefaultTransactionDAO.class);

    public DefaultTransactionDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<TransactionEntity> readAccountTransactions(long accountId, long offset, long count){
        Session session = sessionFactory.getCurrentSession();
        return session
                .createQuery("FROM Transaction transaction" +
                        " WHERE transaction.bankAccount.id = :accountId ORDER BY transaction.id DESC")
                .setMaxResults((int)count)
                .setFirstResult((int)offset)
                .setParameter("accountId", accountId)
                .list();
    }

    @Override
    public long getAccountTransactionsCount(long accountId){
        Session session = sessionFactory.getCurrentSession();
        return (Long) session
                .createQuery("SELECT COUNT(*) FROM Transaction transaction" +
                        " WHERE transaction.bankAccount.id = :accountId")
                .setParameter("accountId", accountId)
                .getSingleResult();
    }
}

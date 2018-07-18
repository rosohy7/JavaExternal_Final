package external.letiuka.persistence.dal;

import external.letiuka.persistence.dal.dao.*;
import external.letiuka.persistence.transaction.TransactionManager;
import org.hibernate.SessionFactory;

/**
 * Provides instances of DAO-classes and injects them with transaction manager.
 * Only to be used by lifecycle manager.
 */
public class DAOFactory {
    private final TransactionManager manager;
    private final SessionFactory sessionFactory;

    public DAOFactory(TransactionManager manager, SessionFactory sessionFactory)
    {
        this.manager = manager;
        this.sessionFactory = sessionFactory;
    }

    public UserDAO getUserDAO() {
        return new DefaultUserDAO(manager, sessionFactory);
    }

    public BankAccountDAO getBankAccountDAO() {
        return new DefaultBankAccountDAO(manager);
    }

    public TransactionDAO getTransactionDAO() {
        return new DefaultTransactionDAO(manager);
    }

    public ScheduledDAO getScheduledDAO() {
        return new DefaultScheduledDAO(manager);
    }
}

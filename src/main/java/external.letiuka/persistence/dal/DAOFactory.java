package external.letiuka.persistence.dal;

import external.letiuka.persistence.dal.dao.*;
import external.letiuka.persistence.transaction.TransactionManager;

/**
 * Provides instances of DAO-classes and injects them with transaction manager.
 * Only to be used by lifecycle manager.
 */
public class DAOFactory {
    private final TransactionManager manager;

    public DAOFactory(TransactionManager manager) {
        this.manager = manager;
    }

    public UserDAO getUserDAO() {
        return new DefaultUserDAO(manager);
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

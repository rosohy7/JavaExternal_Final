package external.letiuka.persistence.dal;

import external.letiuka.persistence.dal.dao.*;
import external.letiuka.persistence.transaction.TransactionManager;

public class DefaultDAOFactory implements DAOFactory {
    private TransactionManager manager;

    public DefaultDAOFactory(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public UserDAO getUserDAO() {
        return new DefaultUserDAO(manager);
    }

    @Override
    public BankAccountDAO getBankAccountDAO() {
        return new DefaultBankAccountDAO(manager);
    }

    @Override
    public TransactionDAO getTransactionDAO() {
        return new DefaultTransactionDAO(manager);
    }

    @Override
    public ScheduledDAO getScheduledDAO() {
        return new DefaultScheduledDAO(manager);
    }
}

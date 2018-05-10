package external.letiuka.service;

import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;

public class DefaultServiceFactory implements ServiceFactory {
    TransactionManager manager;

    public DefaultServiceFactory(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public DefaultAuthenticationService getAuthentificationService(UserDAO userDAO) {
        return new DefaultAuthenticationService(manager, userDAO);
    }
}

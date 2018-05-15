package external.letiuka.service;

import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.security.PasswordHasher;

public class DefaultServiceFactory implements ServiceFactory {
    TransactionManager manager;

    public DefaultServiceFactory(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public DefaultAuthenticationService getAuthentificationService(UserDAO userDAO, PasswordHasher hasher) {
        return new DefaultAuthenticationService(manager, userDAO, hasher);
    }

    @Override
    public BankAccountService getBankAccountService(TimeProvider timeProvider, InterestRateProvider interestRateProvider, AccountNumberGenerator numberGenerator, UserDAO userDAO, BankAccountDAO accountDAO) {
        return new DefaultBankAccountService(manager,timeProvider,interestRateProvider,numberGenerator,userDAO,accountDAO);
    }
}

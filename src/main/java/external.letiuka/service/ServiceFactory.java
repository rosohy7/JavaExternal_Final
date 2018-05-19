package external.letiuka.service;

import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.TransactionDAO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.security.PasswordHasher;

public class ServiceFactory{
    private final TransactionManager manager;

    public ServiceFactory(TransactionManager manager) {
        this.manager = manager;
    }

    public DefaultAuthenticationService getAuthentificationService(UserDAO userDAO, PasswordHasher hasher) {
        return new DefaultAuthenticationService(manager, userDAO, hasher);
    }

    public BankAccountService getBankAccountService(
            TimeProvider timeProvider, InterestRateProvider interestRateProvider,
            TransactionFeeProvider feeProvider, AccountNumberGenerator numberGenerator,
            UserDAO userDAO, BankAccountDAO accountDAO,
            TransactionDAO transactionDAO) {
        return new DefaultBankAccountService(
                manager,timeProvider,interestRateProvider,
                numberGenerator,userDAO,accountDAO,feeProvider,
                transactionDAO);
    }
}

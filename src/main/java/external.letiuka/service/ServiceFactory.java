package external.letiuka.service;

import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.TransactionDAO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.security.PasswordHasher;
import external.letiuka.service.domain.AccountNumberGenerator;
import external.letiuka.service.domain.InterestRateProvider;
import external.letiuka.service.domain.TimeProvider;
import external.letiuka.service.domain.TransactionFeeProvider;

/**
 * Provides instances of service classes and injects them with transaction manager.
 * Only to be used by lifecycle manager.
 */
public class ServiceFactory{
    private final TransactionManager manager;

    public ServiceFactory(TransactionManager manager) {
        this.manager = manager;
    }

    public DefaultAuthenticationService getAuthentificationService(UserDAO userDAO, PasswordHasher hasher) {
        return new DefaultAuthenticationService(manager, userDAO, hasher);
    }

    public BankOperationsService getBankAccountService(
            TimeProvider timeProvider, InterestRateProvider interestRateProvider,
            TransactionFeeProvider feeProvider, AccountNumberGenerator numberGenerator,
            UserDAO userDAO, BankAccountDAO accountDAO,
            TransactionDAO transactionDAO) {
        return new DefaultBankOperationsService(
                manager,timeProvider,interestRateProvider,
                numberGenerator,userDAO,accountDAO,feeProvider,
                transactionDAO);
    }
}

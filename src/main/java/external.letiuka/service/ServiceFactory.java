package external.letiuka.service;

import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.security.PasswordHasher;

public interface ServiceFactory {
    AuthenticationService getAuthentificationService(UserDAO userDAO, PasswordHasher hasher);
    BankAccountService getBankAccountService
            (TimeProvider timeProvider,
             InterestRateProvider interestRateProvider, AccountNumberGenerator numberGenerator,
             UserDAO userDAO, BankAccountDAO accountDAO);
}

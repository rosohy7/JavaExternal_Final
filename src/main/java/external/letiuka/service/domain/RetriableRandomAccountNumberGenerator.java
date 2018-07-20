package external.letiuka.service.domain;

import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.entities.BankAccountEntity;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;

/**
 * Generates random account numbers and checks with database if they are taken
 */
@Component
public class RetriableRandomAccountNumberGenerator implements AccountNumberGenerator {
    private static final Logger logger = Logger.getLogger(RetriableRandomAccountNumberGenerator.class);
    private BankAccountDAO accountDAO;

    @Autowired
    public RetriableRandomAccountNumberGenerator(BankAccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    @Retryable(maxAttempts = 200,value = CollisionException.class)
    public String getNewAccountNumber() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; ++i) {
            int number = (int) (Math.random() * 10);
            builder.append(Integer.toString(number));
        }
        String accountNumber = builder.toString();
        BankAccountEntity account = accountDAO.readAccount(accountNumber);
        if (account != null)
        {
            logger.log(Level.WARN, "Collision happened when generating card number " + accountNumber);
            throw new CollisionException("Generated account number that already exists");
        }
        return accountNumber;
    }

    @Recover
    public void handleFail(CollisionException e)
    {
        logger.log(Level.ERROR,"Failed to generate a unique bank account number");
        throw new PersistenceException(e);
    }
}

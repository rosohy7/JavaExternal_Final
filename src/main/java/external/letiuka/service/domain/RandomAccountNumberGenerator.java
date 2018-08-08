package external.letiuka.service.domain;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.entities.BankAccountEntity;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Generates random account numbers and checks with database if they are taken
 */
public class RandomAccountNumberGenerator implements AccountNumberGenerator {
    private static final Logger logger = Logger.getLogger(RandomAccountNumberGenerator.class);
    private final BankAccountDAO accountDAO;

    public RandomAccountNumberGenerator(BankAccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public String getNewAccountNumber() {
        String accountNumber = null;
        StringBuilder builder = null;
        boolean success = false;
        int attempts = 1000;
        while (!success && attempts-- > 0) {
            builder = new StringBuilder();
            for (int i = 0; i < 16; ++i) {
                int number = (int) (Math.random() * 10);
                builder.append(Integer.toString(number));
            }
            accountNumber = builder.toString();
            try {
                BankAccountEntity account = accountDAO.readAccount(accountNumber);
                if (account == null) success = true;
                else {
                    logger.log(Level.WARN, "Colllision happened when generating card number " + accountNumber);
                }
            } catch (DAOException e) {
                return null;
            }
        }
        logger.log(Level.INFO, "Generated new card number " + accountNumber);
        return builder.toString();
    }
}

package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.BankAccountEntity;

import java.sql.Date;
import java.util.List;

/**
 * Provides interface for accessing bank accounts in a database
 */
public interface BankAccountDAO {
    BankAccountEntity readAccount(String accountNumber);

    List<BankAccountEntity> readUserAccounts(long userId, long offset, long count);

    List<BankAccountEntity> readUserAccounts(String login, long offset, long count);

    List<BankAccountEntity> readUnconfirmedAccounts
            (long offset, long count);

    long getUnconfirmedAccountCount();

    long getUserAccountCount(String login);

}

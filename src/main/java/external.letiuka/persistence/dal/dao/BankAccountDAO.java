package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.BankAccountEntity;

import java.sql.Date;
import java.util.List;

public interface BankAccountDAO {
    void insertAccount(BankAccountEntity account) throws DAOException;
    BankAccountEntity readAccount(long id) throws DAOException;
    BankAccountEntity readAccount(String accountNumber) throws DAOException;
    List<BankAccountEntity> readUserAccounts(long userId, long offset, long count) throws DAOException;
    List<BankAccountEntity> readUserAccounts(String login, long offset, long count) throws DAOException;
    List<BankAccountEntity> readUnconfirmedAccounts
            (long offset, long count) throws DAOException;
    long getUnconfirmedAccountCount() throws DAOException;
    long getUserAccountCount(String login) throws DAOException;
    void updateAccount(BankAccountEntity account) throws DAOException;
    void confirmAccount(String accountNumber, Date expires) throws DAOException;
    void deleteAccount(long id) throws DAOException;

}

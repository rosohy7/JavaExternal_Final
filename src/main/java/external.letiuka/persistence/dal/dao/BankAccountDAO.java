package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.BankAccountEntity;

import java.sql.Date;
import java.util.List;

public interface BankAccountDAO {
    void insertAccount(BankAccountEntity user) throws DAOException;
    BankAccountEntity readAccount(long id) throws DAOException;
    BankAccountEntity readAccount(String accountNumber) throws DAOException;
    List<BankAccountEntity> readUserAccounts(long userId) throws DAOException;
    List<BankAccountEntity> readUnconfirmedAccounts
            (long offset, long count) throws DAOException;
    long getUnconfirmedAccountCount() throws DAOException;
    void updateAccount(BankAccountEntity user) throws DAOException;
    void confirmAccount(String accountNumber, Date expires) throws DAOException;
    void deleteAccount(long id) throws DAOException;

}

package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.BankAccountEntity;

import java.util.List;

public interface BankAccountDAO {
    void insertAccount(BankAccountEntity user) throws DAOException;
    BankAccountEntity readAccount(long id) throws DAOException;
    BankAccountEntity readAccount(String accountNumber) throws DAOException;
    List<BankAccountEntity> readUserAccounts(long userId) throws DAOException;
    void updateAccount(BankAccountEntity user) throws DAOException;
    void deleteAccount(long id) throws DAOException;

}

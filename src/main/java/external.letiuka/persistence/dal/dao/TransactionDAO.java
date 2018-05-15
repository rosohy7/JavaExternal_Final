package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.TransactionEntity;

public interface TransactionDAO {
    void insertTransaction(TransactionEntity user) throws DAOException;
    TransactionEntity readTransaction(long id) throws DAOException;
    void deleteTransaction(long id) throws DAOException;
}

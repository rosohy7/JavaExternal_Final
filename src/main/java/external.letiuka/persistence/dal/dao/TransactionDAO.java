package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.TransactionEntity;

import java.util.List;

/**
 * Provides interface for accessing bank transactions in a database
 */
public interface TransactionDAO {
    void insertTransaction(TransactionEntity transaction) throws DAOException;

    TransactionEntity readTransaction(long id) throws DAOException;

    void deleteTransaction(long id) throws DAOException;

    void updatePairId(long id, Long pairId) throws DAOException;

    List<TransactionEntity> readAccountTransactions(long accountId, long offset, long count) throws DAOException;

    long getAccountTransactionsCount(long accountId) throws DAOException;
}

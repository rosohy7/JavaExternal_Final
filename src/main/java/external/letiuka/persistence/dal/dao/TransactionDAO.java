package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.TransactionEntity;

import java.util.List;

/**
 * Provides interface for accessing bank transactions in a database
 */
public interface TransactionDAO {
    List<TransactionEntity> readAccountTransactions
            (long accountId, long offset, long count);

    long getAccountTransactionsCount(long accountId);
}

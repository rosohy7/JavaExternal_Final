package external.letiuka.persistence.transaction;

import java.sql.Connection;

public interface TransactionManager {
    void beginTransaction() throws TransactionException;
    void commit() throws TransactionException;
    void rollback() throws TransactionException;
    Connection getConnection() throws TransactionException;
    void returnConnection(boolean successful) throws TransactionException;
}

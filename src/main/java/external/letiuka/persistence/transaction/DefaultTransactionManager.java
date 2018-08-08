package external.letiuka.persistence.transaction;

import external.letiuka.persistence.connectionpool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class DefaultTransactionManager implements TransactionManager {
    private final ThreadLocal<Transaction> transaction = new ThreadLocal<>();
    private final ConnectionPool pool;

    public DefaultTransactionManager(ConnectionPool pool) {
        this.pool = pool;
    }

    private Transaction createTransaction(boolean explicit) throws TransactionException {
        Connection cn;
        Transaction tr = transaction.get();
        if (tr != null) {
            throw new TransactionException("A transaction is already active");
        }
        try {
            cn = pool.getConnection();
            cn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
        tr = new Transaction(cn, explicit);
        transaction.set(tr);
        return tr;
    }

    @Override
    public void beginTransaction() throws TransactionException {
        createTransaction(true);
    }

    private void endTransaction() throws TransactionException {
        Transaction tr = transaction.get();
        Connection cn = tr.getConnection();
        if (cn == null) {
            throw new TransactionException();
        }
        try {
            cn.close();
        } catch (SQLException e) {
            throw new TransactionException(e);
        } finally {
            transaction.remove();
        }

    }

    @Override
    public void commit() throws TransactionException {
        Transaction tr = transaction.get();
        Connection cn = tr.getConnection();
        if (cn == null) {
            throw new TransactionException();
        }
        try {
            cn.commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        } finally {
            endTransaction();
        }
    }

    @Override
    public void rollback() throws TransactionException {
        Transaction tr = transaction.get();
        Connection cn = tr.getConnection();
        if (cn == null) {
            throw new TransactionException();
        }
        try {
            cn.rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        } finally {
            endTransaction();
        }
    }

    @Override
    public Connection getConnection() throws TransactionException {
        Transaction tr = transaction.get();
        if (transaction.get() == null)
            tr = createTransaction(false);
        return tr.getConnection();
    }

    @Override
    public void returnConnection(boolean successful) throws TransactionException {
        Transaction tr = transaction.get();
        if (!tr.isExplicit()) {
            if (successful) commit();
            else rollback();
        }
    }

    class Transaction {
        private Connection connection;
        private boolean explicit;

        private Transaction(Connection connection, boolean explicit) {

            this.connection = connection;
            this.explicit = explicit;
        }

        private Connection getConnection() {
            return connection;
        }

        private boolean isExplicit() {
            return explicit;
        }
    }
}
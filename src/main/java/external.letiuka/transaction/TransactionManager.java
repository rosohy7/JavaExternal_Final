package external.letiuka.transaction;

import external.letiuka.connectionpool.ConnectionPool;
import external.letiuka.exception.TransactionException;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private static final ThreadLocal<WrappedConnection> connection = new ThreadLocal<>();

    private TransactionManager() {
        throw new UnsupportedOperationException("Cannot create an instance of TransactionManager");
    }

    private static void beginTransaction(boolean autoCommit) throws TransactionException {
        WrappedConnection wrapped = connection.get();
        if (wrapped != null) {
            throw new TransactionException();
        }
        try {
            Connection cn = ConnectionPool.getInstance().getConnection();
            cn.setAutoCommit(autoCommit);
            wrapped = new WrappedConnection(cn, autoCommit);
            connection.set(wrapped);
        } catch (NamingException e) {
            throw new TransactionException(e);
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    public static void beginTransaction() throws TransactionException {
        beginTransaction(false);
    }

    private static void endTransaction() throws TransactionException {
        WrappedConnection wrapped = connection.get();
        if (wrapped != null) {
            throw new TransactionException();
        }
        try {
            wrapped.close();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
        connection.set(null);

    }

    public static void commit() throws TransactionException {
        WrappedConnection wrapped = connection.get();
        if (wrapped != null) {
            throw new TransactionException();
        }
        try {
            wrapped.commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        } finally {
            endTransaction();
        }
    }

    public static void rollback() throws TransactionException {
        WrappedConnection wrapped = connection.get();
        if (wrapped != null) {
            throw new TransactionException();
        }
        try {
            wrapped.rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        } finally {
            endTransaction();
        }
    }

    public static WrappedConnection getConnection() throws TransactionException {
        WrappedConnection wrapped = connection.get();
        if (wrapped == null)
            beginTransaction(true);
        return wrapped;
    }

    public static void returnConnection() throws TransactionException {
        WrappedConnection wrapped = connection.get();
        if (wrapped.getAutoCommit()) endTransaction();
    }
}

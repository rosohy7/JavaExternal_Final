package external.letiuka.transaction;

import external.letiuka.connectionpool.ConnectionPool;
import external.letiuka.exception.TransactionException;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private static final ThreadLocal<Connection> connection = new ThreadLocal<>();

    private TransactionManager() {
        throw new UnsupportedOperationException("Cannot create an instance of TransactionManager");
    }

    private static void beginTransaction(boolean autoCommit) throws TransactionException {
        Connection cn = connection.get();
        if (cn != null) {
            throw new TransactionException();
        }
        try {
            cn = ConnectionPool.getInstance().getConnection();
            cn.setAutoCommit(autoCommit);
        } catch (NamingException e) {
            throw new TransactionException(e);
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
        connection.set(cn);
    }

    public static void beginTransaction() throws TransactionException {
        beginTransaction(false);
    }

    private static void endTransaction() throws TransactionException {
        Connection cn = connection.get();
        if (cn != null) {
            throw new TransactionException();
        }
        try {
            cn.close();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
        connection.set(null);

    }

    public static void commit() throws TransactionException {
       Connection cn = connection.get();
        if (cn != null) {
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

    public static void rollback() throws TransactionException {
        Connection cn = connection.get();
        if (cn != null) {
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

    public static Connection getConnection() throws TransactionException {
        Connection cn = connection.get();
        if (cn == null)
            beginTransaction(true);
        return cn;
    }

    public static void returnConnection() throws TransactionException {
        Connection cn = connection.get();
        try {
            if (cn.getAutoCommit()) endTransaction();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }
}
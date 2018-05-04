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
        WrappedConnection con = connection.get();
        if (con != null) {
            throw new TransactionException();
        }
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            connection.setAutoCommit(autoCommit);
            con = new WrappedConnection(connection, autoCommit);
            TransactionManager.connection.set(con);
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
        WrappedConnection con = connection.get();
        if (con != null) {
            throw new TransactionException();
        }
        try {
            con.close();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
        connection.set(null);

    }

    public static void commit() throws TransactionException {
        WrappedConnection con = connection.get();
        if (con != null) {
            throw new TransactionException();
        }
        try {
            con.commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        } finally {
            endTransaction();
        }
    }

    public static void rollback() throws TransactionException {
        WrappedConnection con = connection.get();
        if (con != null) {
            throw new TransactionException();
        }
        try {
            con.rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        } finally {
            endTransaction();
        }
    }

    public static WrappedConnection getConnection() throws TransactionException {
        WrappedConnection con = connection.get();
        if (con == null)
            beginTransaction(true);
        return con;
    }

    public static void returnConnection() throws TransactionException {
        WrappedConnection con = connection.get();
        if (con.getAutoCommit()) endTransaction();
    }
}

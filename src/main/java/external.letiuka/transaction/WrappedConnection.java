package external.letiuka.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WrappedConnection {
    private Connection cn;
    private boolean autoCommit;

    public WrappedConnection(Connection cn, boolean autoCommit) {
        this.cn = cn;
        this.autoCommit = autoCommit;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return cn.prepareStatement(sql);
    }

    boolean getAutoCommit() {
        return autoCommit;
    }

    public void commit() throws SQLException {
        cn.commit();
    }

    public void rollback() throws SQLException {
        cn.rollback();
    }

    public void close() throws SQLException {
        cn.close();
    }

}

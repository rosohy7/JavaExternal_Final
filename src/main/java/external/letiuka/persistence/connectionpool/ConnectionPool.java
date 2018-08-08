package external.letiuka.persistence.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides sql-connections to transaction manager
 */
public interface ConnectionPool {
    Connection getConnection() throws SQLException;
}

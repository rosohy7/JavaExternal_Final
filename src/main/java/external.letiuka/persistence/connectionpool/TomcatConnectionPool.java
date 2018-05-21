package external.letiuka.persistence.connectionpool;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Tomcat connection poool with configuration in constructor
 */
public class TomcatConnectionPool implements ConnectionPool {

    private final DataSource ds;

    public TomcatConnectionPool(){
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://localhost:3306/bankapp?useSSL=false&characterEncoding=utf8");
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername("bankapp");
        p.setPassword("bankapp");
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        ds = new DataSource();
        ds.setPoolProperties(p);
    }
    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();

    }

}

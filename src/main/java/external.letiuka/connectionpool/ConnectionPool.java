package external.letiuka.connectionpool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static ConnectionPool pool;
    private final DataSource ds;

    private ConnectionPool() throws NamingException{
        Context initialContext = new InitialContext();
        Context environmentContext = (Context) initialContext.lookup("java:comp/env");
        ds = (DataSource) environmentContext.lookup("jdbc/JCGExampleDB");

    }

    public static synchronized ConnectionPool getInstance() throws NamingException{
        if(pool == null){
            pool =new ConnectionPool();
        }
        return pool;

    }




    public Connection getConnection() throws SQLException {
        return ds.getConnection();

    }

}

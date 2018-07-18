package external.letiuka.persistence.transaction;

import org.hibernate.Session;

import java.sql.Connection;

public interface TransactionManager {
    void beginTransaction() ;
    void commit() ;
    void rollback() ;
    Session getSession() ;
}

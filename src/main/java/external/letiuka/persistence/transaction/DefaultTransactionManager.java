package external.letiuka.persistence.transaction;

import external.letiuka.persistence.connectionpool.ConnectionPool;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DefaultTransactionManager implements TransactionManager {
    private final ThreadLocal<Session> sessionContainer = new ThreadLocal<>();
    private final SessionFactory sessionFactory;

    public DefaultTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session createTransaction(boolean explicit)  {
        Session tr;
        tr = sessionFactory.openSession();
        tr.beginTransaction();
        sessionContainer.set(tr);
        return tr;
    }

    @Override
    public void beginTransaction() {
        createTransaction(true);
    }

    private void endSession()  {
        Session tr = sessionContainer.get();
        tr.close();
        sessionContainer.remove();

    }

    @Override
    public void commit() {
        Session tr = sessionContainer.get();
        tr.getTransaction().commit();
        endSession();
    }

    @Override
    public void rollback() {
        Session tr = sessionContainer.get();
        tr.getTransaction().rollback();
        endSession();
    }

    @Override
    public Session getSession(){
        Session tr = sessionContainer.get();
        if (sessionContainer.get() == null)
            tr = createTransaction(false);
        return tr;
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
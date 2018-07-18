//package external.letiuka.persistence.transaction;
//
//
//import external.letiuka.persistence.connectionpool.ConnectionPool;
//import external.letiuka.persistence.transaction.DefaultTransactionManager;
//import external.letiuka.persistence.transaction.TransactionException;
//import external.letiuka.persistence.transaction.TransactionManager;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import static junit.framework.TestCase.assertEquals;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.core.IsNot.not;
//import static org.junit.Assert.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DefaultTransactionManagerTest extends TransactionManagerTest {
//
//    @Override
//    protected TransactionManager getManagerInstance() {
//        return new DefaultTransactionManager(pool);
//    }
//}

package external.letiuka.persistence.transaction;

import external.letiuka.persistence.connectionpool.ConnectionPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;

public abstract class TransactionManagerTest {
    @Mock
    protected ConnectionPool pool;
    @Mock
    protected Connection cona,conb;

    TransactionManager manager;

    protected abstract TransactionManager getManagerInstance();
    @Before
    public void init(){
        manager = getManagerInstance();
    }


    @Test(timeout = 1000)
    public void commitsImplicitTransactions() throws SQLException, TransactionException {
        Mockito.when(pool.getConnection()).thenReturn(
                cona).thenReturn(conb);
        Connection cn1 = manager.getConnection();
        manager.returnConnection(true);
        Connection cn2 = manager.getConnection();
        manager.returnConnection(false); //different connection and transaction
        Mockito.verify(cn1).commit();
        Mockito.verify(cn1,never()).rollback();
    }

    @Test(timeout = 1000)
    public void rollsBackImplicitTransactions() throws SQLException, TransactionException {
        Mockito.when(pool.getConnection()).thenReturn(
                cona).thenReturn(conb);
        Connection cn1 = manager.getConnection();
        manager.returnConnection(false);
        Connection cn2 = manager.getConnection();
        manager.returnConnection(true); //different connection and transaction
        Mockito.verify(cn1,never()).commit();
        Mockito.verify(cn1).rollback();
    }

    @Test(timeout = 1000)
    public void commitsExplicitTransactions() throws SQLException, TransactionException {
        Mockito.when(pool.getConnection()).thenReturn(
                cona).thenReturn(conb);
        manager.beginTransaction();
        Connection cn1 = manager.getConnection();
        manager.returnConnection(true);
        manager.getConnection();
        manager.returnConnection(true); // same connection and transaction
        manager.getConnection();
        Mockito.verify(cn1,never()).commit();
        manager.commit();
        Mockito.verify(cn1).commit();
        Mockito.verify(cn1,never()).rollback();
    }

    @Test(timeout = 1000)
    public void rollsBackExplicitTransactions() throws SQLException, TransactionException {
        Mockito.when(pool.getConnection()).thenReturn(
                cona).thenReturn(conb);
        manager.beginTransaction();
        Connection cn1 = manager.getConnection();
        manager.returnConnection(true);
        manager.getConnection();
        manager.returnConnection(false); // same connection and transaction
        Mockito.verify(cn1,never()).commit();
        manager.rollback();
        Mockito.verify(cn1,never()).commit();
        Mockito.verify(cn1).rollback();
    }
    @Test(timeout = 1000)
    public void startsNewExplicitTransactions() throws SQLException, TransactionException {
        Mockito.when(pool.getConnection()).thenReturn(
                cona).thenReturn(conb);
        manager.beginTransaction();
        Connection cn1=manager.getConnection();
        manager.returnConnection(true);
        manager.commit();
        manager.beginTransaction();
        Connection cn2=manager.getConnection();
        manager.returnConnection(true);
        manager.commit();
        assertThat(cn1, is(not(cn2)));

    }
    @Test(timeout = 1000)
    public void startsNewImplicitTransactions() throws SQLException, TransactionException {
        Mockito.when(pool.getConnection()).thenReturn(
                cona).thenReturn(conb);
        Connection cn1=manager.getConnection();
        manager.returnConnection(true);
        Connection cn2=manager.getConnection();
        manager.returnConnection(true);
        assertThat(cn1, is(not(cn2)));

    }
}

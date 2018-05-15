package external.letiuka.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public abstract class TimeProviderTest {
    TimeProvider timeProvider;
    protected abstract TimeProvider getTimeProvider();
    @Before
    public void init(){
        timeProvider=getTimeProvider();
    }
    @Test(timeout = 1000)
    public void returnsTimesStamp(){
        assertNotNull(timeProvider.getCurrentTimestamp());
    }
}

//package external.letiuka.service;
//
//import external.letiuka.service.domain.TimeProvider;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.sql.Timestamp;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//public abstract class TimeProviderTest {
//    TimeProvider timeProvider;
//    protected abstract TimeProvider getTimeProvider();
//    @Before
//    public void init(){
//        timeProvider=getTimeProvider();
//    }
//    @Test(timeout = 10000)
//    public void returnsTimesstampsInChronologialOrder() throws InterruptedException {
//        Timestamp time1=timeProvider.getCurrentTimestamp();
//        Thread.sleep(2);
//        Timestamp time2=timeProvider.getCurrentTimestamp();
//        assertTrue("Timestamp taken before is before", time1.before(time2));
//
//    }
//
//    @Test(timeout = 10000)
//    public void returnsTimeInChronologialOrder() throws InterruptedException {
//        long time1, time2;
//        time1=timeProvider.getCurrentMillis();
//        Thread.sleep(2);
//        time2=timeProvider.getCurrentMillis();
//        assertTrue("Time taken before has smaller value", time1<time2);
//    }
//
//    @Test(timeout = 10000)
//    public void returnsTimeDaysLaterInChronologialOrder() throws InterruptedException {
//        long time1, time2,time3,time0;
//        time3=timeProvider.getMillisDaysLater(11);
//        time1=timeProvider.getMillisDaysLater(10);
//        Thread.sleep(2);
//        time2=timeProvider.getMillisDaysLater(10);
//        time0=timeProvider.getMillisDaysLater(0);
//        assertTrue("Now is sooner than in 10 days", time0<time1);
//        assertTrue("Time taken before has smaller value", time1<time2);
//        assertTrue("10 days later is sooner than 11 days later", time2<time3);
//
//    }
//
//}

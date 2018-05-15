package external.letiuka.service;

public class RealTimeProviderTest extends TimeProviderTest {
    @Override
    protected TimeProvider getTimeProvider() {
        return new RealTimeProvider();
    }
}

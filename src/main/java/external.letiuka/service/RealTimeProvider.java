package external.letiuka.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RealTimeProvider implements TimeProvider {
    @Override
    public long getCurrentMillis() {
        return Instant.ofEpochSecond(0).until(Instant.now(), ChronoUnit.MILLIS);
    }

    @Override
    public Timestamp getCurrentTimestamp() {
        return new Timestamp(getCurrentMillis());
    }

    @Override
    public long getMillisDaysLater(int years) {
        Instant later = Instant.now().plus(years,ChronoUnit.DAYS);
        return Instant.ofEpochSecond(0).until(later, ChronoUnit.MILLIS);
    }

}

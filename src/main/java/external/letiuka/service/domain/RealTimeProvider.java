package external.letiuka.service.domain;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
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
    public long getMillisDaysLater(int days) {
        Instant later = Instant.now().plus(days,ChronoUnit.DAYS);
        return Instant.ofEpochSecond(0).until(later, ChronoUnit.MILLIS);
    }

}

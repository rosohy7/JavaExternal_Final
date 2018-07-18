package external.letiuka.service.domain;

import java.sql.Timestamp;
/**
 * Responsible for providing services with time now and in the future
 */
public interface TimeProvider {
    long getCurrentMillis();

    Timestamp getCurrentTimestamp();

    long getMillisDaysLater(int days);
}
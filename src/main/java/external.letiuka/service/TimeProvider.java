package external.letiuka.service;

import java.sql.Timestamp;

public interface TimeProvider {
    long getCurrentMillis();

    Timestamp getCurrentTimestamp();

    long getMillisDaysLater(int days);
}
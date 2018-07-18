package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;


public interface ScheduledDAO {
    void accumulateInterest();
    void flushInterest();
}

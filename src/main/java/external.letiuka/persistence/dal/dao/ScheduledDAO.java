package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;

public interface ScheduledDAO {
    void accumulateInterest() throws DAOException;
    void flushInterest() throws DAOException;
}

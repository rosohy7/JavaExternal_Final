package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;

/**
 * Provides interface for performing scheduled jobs in a database
 */
public interface ScheduledDAO {
    void accumulateInterest() throws DAOException;
    void flushInterest() throws DAOException;
}

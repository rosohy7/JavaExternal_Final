package external.letiuka.service.scheduled;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.ScheduledDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;

@Component
public class DailyInterestAccumulation{
    private static Logger logger = Logger.getLogger(DailyInterestAccumulation.class);
    private final TransactionManager manager;

    private ScheduledDAO scheduledDAO;

    @Autowired
    public DailyInterestAccumulation(TransactionManager manager, ScheduledDAO scheduledDAO) {
        this.manager = manager;
        this.scheduledDAO = scheduledDAO;
    }

    @Scheduled(cron = "0 * * * * *") // Daily 1:00 a. m. "0 0 1 1/1 * ? *" // Every minute "0 0/1 * 1/1 * ? *"
    public void execute() {
        try{
            manager.beginTransaction();
            logger.log(Level.INFO, "Starting daily interest accumulation job");
            scheduledDAO.accumulateInterest();
            manager.commit();
        } catch(PersistenceException e){
            manager.rollback();
            logger.log(Level.ERROR, "Failed daily interest accumulation job");
        }

    }
}

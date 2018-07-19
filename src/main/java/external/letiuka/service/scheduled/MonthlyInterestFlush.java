package external.letiuka.service.scheduled;

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
public class MonthlyInterestFlush{
    private static Logger logger = Logger.getLogger(MonthlyInterestFlush.class);
    private final TransactionManager manager;

    private ScheduledDAO scheduledDAO;

    @Autowired
    public MonthlyInterestFlush(TransactionManager manager, ScheduledDAO scheduledDAO) {
        this.manager = manager;
        this.scheduledDAO = scheduledDAO;
    }

    // Monthly, first day, 2:00 a. m. "0 0 2 1 1/1 ? *" //Every 5 minutes, "0 0/5 * 1/1 * ? *"
    @Scheduled(cron = "0 */5 * * * *")
    public void execute(){
        try{
            manager.beginTransaction();
            logger.log(Level.INFO, "Starting monthly interest flush job");
            scheduledDAO.flushInterest();
            manager.commit();
        } catch(PersistenceException e){
            manager.rollback();
            logger.log(Level.ERROR, "Failed monthly interestr flush job");
        }

    }
}

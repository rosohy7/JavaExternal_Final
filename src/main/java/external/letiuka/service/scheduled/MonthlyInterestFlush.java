package external.letiuka.service.scheduled;

import external.letiuka.persistence.dal.dao.ScheduledDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class MonthlyInterestFlush implements Job {
    private static Logger logger = Logger.getLogger(MonthlyInterestFlush.class);
    private final TransactionManager manager;

    public MonthlyInterestFlush(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            manager.beginTransaction();
            logger.log(Level.INFO, "Starting monthly interest flush job");
            ScheduledDAO dao = (ScheduledDAO)jobExecutionContext.getScheduler().getContext().get("ScheduledDAO");
            dao.flushInterest();
            manager.commit();
        } catch(Exception e){
            manager.rollback();
            throw new JobExecutionException(e);
        }

    }
}

package external.letiuka.service.scheduled;

import external.letiuka.persistence.dal.dao.ScheduledDAO;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class MonthlyInterestFlush implements Job {
    private static Logger logger = Logger.getLogger(MonthlyInterestFlush.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            logger.log(Level.INFO, "Starting monthly interest flush job");
            ScheduledDAO dao = (ScheduledDAO)jobExecutionContext.getScheduler().getContext().get("ScheduledDAO");
            dao.flushInterest();
        } catch(Exception e){
            logger.log(Level.ERROR, "Failed monthly interest flush job");
            throw new JobExecutionException(e);
        }

    }
}

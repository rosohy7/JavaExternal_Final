package external.letiuka.service.scheduled;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.ScheduledDAO;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DailyInterestAccumulation implements Job {
    private static Logger logger = Logger.getLogger(DailyInterestAccumulation.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            logger.log(Level.INFO, "Starting daily interest accumulation job");
            ScheduledDAO dao = (ScheduledDAO)jobExecutionContext.getScheduler().getContext().get("ScheduledDAO");
            dao.accumulateInterest();
        } catch(Exception e){
            logger.log(Level.ERROR, "Failed daily interest accumulation job");
            throw new JobExecutionException(e);
        }

    }
}

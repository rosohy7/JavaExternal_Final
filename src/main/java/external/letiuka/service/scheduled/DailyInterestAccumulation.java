package external.letiuka.service.scheduled;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.ScheduledDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class DailyInterestAccumulation implements Job {
    private static Logger logger = Logger.getLogger(DailyInterestAccumulation.class);
    private final TransactionManager manager;

    public DailyInterestAccumulation(TransactionManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            manager.beginTransaction();
            logger.log(Level.INFO, "Starting daily interest accumulation job");
            ScheduledDAO dao = (ScheduledDAO)jobExecutionContext.getScheduler().getContext().get("ScheduledDAO");
            dao.accumulateInterest();
            manager.commit();
        } catch(Exception e){
            manager.rollback();
            throw new JobExecutionException(e);
        }

    }
}

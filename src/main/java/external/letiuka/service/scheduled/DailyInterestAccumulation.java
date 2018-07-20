package external.letiuka.service.scheduled;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.ScheduledDAO;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@Component
public class DailyInterestAccumulation {
    private static Logger logger = Logger.getLogger(DailyInterestAccumulation.class);
    private final SessionFactory sessionFactory;
    private final PlatformTransactionManager transactionManager;

    private ScheduledDAO scheduledDAO;

    @Autowired
    public DailyInterestAccumulation(PlatformTransactionManager transactionManager, SessionFactory sessionFactory, ScheduledDAO scheduledDAO) {
        this.sessionFactory = sessionFactory;
        this.scheduledDAO = scheduledDAO;
        this.transactionManager = transactionManager;
    }

    @Transactional(rollbackOn = Exception.class)
    @Scheduled(cron = "0 * * * * *") // Daily 1:00 a. m. "0 0 1 1/1 * ? *" // Every minute "0 0/1 * 1/1 * ? *"
    public void execute() {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
        try {
            logger.log(Level.INFO, "Starting daily interest accumulation job");
            scheduledDAO.accumulateInterest();
            transactionManager.commit(status);
        }
        catch(PersistenceException e){
            transactionManager.rollback(status);
            logger.log(Level.ERROR, "Failed daily interest accumulation job", e );
        }

    }
}

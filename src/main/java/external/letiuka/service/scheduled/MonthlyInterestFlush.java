package external.letiuka.service.scheduled;

import external.letiuka.persistence.dal.dao.ScheduledDAO;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.PersistenceException;

@Component
public class MonthlyInterestFlush {
    private static Logger logger = Logger.getLogger(MonthlyInterestFlush.class);
    private final SessionFactory sessionFactory;
    private final PlatformTransactionManager transactionManager;

    private final ScheduledDAO scheduledDAO;

    @Autowired
    public MonthlyInterestFlush(PlatformTransactionManager transactionManager,
                                SessionFactory sessionFactory, ScheduledDAO scheduledDAO) {
        this.transactionManager = transactionManager;
        this.sessionFactory = sessionFactory;
        this.scheduledDAO = scheduledDAO;
    }

    // Monthly, first day, 2:00 a. m. "0 0 2 1 1/1 ? *" //Every 5 minutes, "0 0/5 * 1/1 * ? *"
    @Scheduled(cron = "0 */5 * * * *")
    public void execute() {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
        try {
            logger.log(Level.INFO, "Starting monthly interest flush job");
            scheduledDAO.flushInterest();
            transactionManager.commit(status);
        } catch (PersistenceException e) {
            transactionManager.rollback(status);
            logger.log(Level.ERROR, "Failed monthly interestr flush job");
        }

    }
}

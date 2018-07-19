package external.letiuka.lifecycle;

import external.letiuka.modelviewcontroller.controller.HttpMethod;
import external.letiuka.modelviewcontroller.controller.concrete.*;
import external.letiuka.modelviewcontroller.controller.mapping.ControllerMapper;
import external.letiuka.modelviewcontroller.controller.mapping.DefaultControllerMapper;
import external.letiuka.modelviewcontroller.model.TransactionType;
import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.ScheduledDAO;
import external.letiuka.persistence.dal.dao.TransactionDAO;
import external.letiuka.persistence.entities.BankAccountEntity;
import external.letiuka.persistence.entities.CreditBankAccountEntity;
import external.letiuka.persistence.entities.DepositBankAccountEntity;
import external.letiuka.persistence.entities.FromTransactionEntity;
import external.letiuka.persistence.entities.PaymentTransactionEntity;
import external.letiuka.persistence.entities.ToTransactionEntity;
import external.letiuka.persistence.entities.TransactionEntity;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.persistence.ormconverter.AccountTypeOrmConverter;
import external.letiuka.persistence.ormconverter.RoleOrmConverter;
import external.letiuka.persistence.ormconverter.TransactionTypeOrmConverter;
import external.letiuka.security.PasswordHasher;
import external.letiuka.security.SHA256HexApacheHasher;
import external.letiuka.security.authorization.AuthorizationManager;
import external.letiuka.security.authorization.ActionMapAuthorizationManager;
import external.letiuka.modelviewcontroller.controller.FrontController;
import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.security.Role;
import external.letiuka.persistence.connectionpool.ConnectionPool;
import external.letiuka.persistence.connectionpool.TomcatConnectionPool;
import external.letiuka.persistence.dal.DAOFactory;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.persistence.transaction.DefaultTransactionManager;
import external.letiuka.service.*;
import external.letiuka.service.domain.*;
import external.letiuka.service.scheduled.DailyInterestAccumulation;
import external.letiuka.service.scheduled.MonthlyInterestFlush;
import external.letiuka.springconfig.RootSpringConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class PlainJavaApplicationLifecycleManager implements ApplicationLifecycleManager {
    private static final Logger logger = Logger.getLogger(PlainJavaApplicationLifecycleManager.class);

    protected AnnotationConfigApplicationContext springContext;

    protected ServletContext servletContext;


    public PlainJavaApplicationLifecycleManager(ServletContext servletContext) {
        if (servletContext == null)
            throw new IllegalArgumentException("Null dependency");
        this.servletContext = servletContext;

    }

    @Override
    public final void build() {
        try {
            setupSpring();
            logger.log(Level.TRACE, "Successfully scheduled jobs");
        } catch (Exception e) {
            String message = "Failed to build application object graph. ";

            logger.log(Level.FATAL, message,e);
            throw new Error(message, e);
        }
    }

    private void setupSpring() {

        springContext = new AnnotationConfigApplicationContext();
        springContext.getBeanFactory().registerSingleton("servletContext",servletContext);
        springContext.register(RootSpringConfig.class);
        springContext.refresh();
    }


    @Override
    public void shutdown() {
        springContext.close();
    }
}

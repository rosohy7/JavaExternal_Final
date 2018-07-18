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

    ConnectionPool pool;
    TransactionManager transactionManager;

    protected SessionFactory sessionFactory;

    protected DAOFactory daoFac;
    protected ServiceFactory serviceFac;

    protected UserDAO userDAO;
    protected BankAccountDAO accountDAO;
    protected TransactionDAO transactionDAO;
    protected ScheduledDAO scheduledDAO;

    protected AuthenticationService authService;
    protected BankOperationsService accountService;

    protected PasswordHasher passwordHasher = new SHA256HexApacheHasher();
    protected TimeProvider timeProvider = new RealTimeProvider();
    protected InterestRateProvider rateProvider =
            new ConstantInterestRateProvider(25, 15);
    protected TransactionFeeProvider feeProvider =
            new ConstantTransactionFeeProvider(1.0, 2.0);
    protected AccountNumberGenerator numberGenerator;

    protected SignUpController signUpController;
    protected LogInController logInController;
    protected LogOutController logOutController;
    protected RegisterBankAccountController registerBankAccountController;
    protected ListUnconfirmedController listUnconfirmedController;
    protected ListUserAccountsController listUserAccountsController;
    protected ConfirmAccountController confirmAccountController;
    protected DenyAccountController denyAccountController;
    protected AccountInfoController accountInfoController;
    protected TransferMoneyController transferMoneyController;
    protected SetLanguageController setLanguageController;
    protected DepositController depositController;
    protected WithdrawController withdrawController;

    protected StaticApplicationContext parentContext;
    protected AnnotationConfigApplicationContext springContext;

    protected ServletContext servletContext;
    private ControllerMapper controllerMapper;
    private AuthorizationManager authorizationManager;
    private FrontController frontController;

    protected Scheduler scheduler;


    public PlainJavaApplicationLifecycleManager(ServletContext servletContext) {
        if (servletContext == null)
            throw new IllegalArgumentException("Null dependency");
        this.servletContext = servletContext;

    }

    @Override
    public final void build() {
        try {
            setupSpring();
            setupScheduled();
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

    private void setupHibernate() {
        Properties hibProps = new Properties();
        hibProps.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        hibProps.put(Environment.USER, "bankapp");
        hibProps.put(Environment.PASS, "bankapp");
        hibProps.put(Environment.URL, "jdbc:mysql://localhost:3306/bankapp?useSSL=false&characterEncoding=utf8");
        hibProps.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        hibProps.put(Environment.HBM2DDL_AUTO, "none");

        sessionFactory = new Configuration()
                .addProperties(hibProps)
                .addAnnotatedClass(UserEntity.class)
                .addAnnotatedClass(BankAccountEntity.class)
                .addAnnotatedClass(CreditBankAccountEntity.class)
                .addAnnotatedClass(DepositBankAccountEntity.class)
                .addAnnotatedClass(TransactionEntity.class)
                .addAnnotatedClass(ToTransactionEntity.class)
                .addAnnotatedClass(FromTransactionEntity.class)
                .addAnnotatedClass(PaymentTransactionEntity.class)
                .addAnnotatedClass(RoleOrmConverter.class)
                .addAnnotatedClass(AccountTypeOrmConverter.class)
                .addAnnotatedClass(TransactionTypeOrmConverter.class)
                .buildSessionFactory();

    }

    protected void createDAL(DAOFactory daoFac) {
        userDAO = daoFac.getUserDAO();
        accountDAO = daoFac.getBankAccountDAO();
        transactionDAO = daoFac.getTransactionDAO();
        scheduledDAO = daoFac.getScheduledDAO();
    }

    protected void createServices(ServiceFactory serFac) {
        numberGenerator = new RandomAccountNumberGenerator(accountDAO);
        authService = serFac.getAuthentificationService(userDAO, passwordHasher);
        accountService = serFac.getBankAccountService(
                timeProvider, rateProvider,
                feeProvider, numberGenerator,
                userDAO, accountDAO, transactionDAO);
    }

    protected void createHttpControllers() {
        signUpController = new SignUpController(authService);
        logInController = new LogInController(authService);
        logOutController = new LogOutController();
        registerBankAccountController = new RegisterBankAccountController(accountService);
        listUnconfirmedController = new ListUnconfirmedController(accountService);
        listUserAccountsController = new ListUserAccountsController(accountService);
        confirmAccountController = new ConfirmAccountController(accountService);
        accountInfoController = new AccountInfoController(accountService);
        denyAccountController = new DenyAccountController(accountService);
        transferMoneyController = new TransferMoneyController(accountService);
        setLanguageController = new SetLanguageController();
        depositController = new DepositController(accountService);
        withdrawController = new WithdrawController(accountService);
    }

    private final AuthorizationManager buildAuthorizationManager() {
        HashMap<String, HashSet<Role>> actionRoleTable = new HashMap<>();
        HashSet<Role> signUpRoles;

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.GUEST);
        actionRoleTable.put("sign-up", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.GUEST);
        actionRoleTable.put("log-in", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.GUEST);
        signUpRoles.add(Role.USER);
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("log-out", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("list-unconfirmed", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.USER);
        actionRoleTable.put("register-account", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.USER);
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("list-accounts", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.USER);
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("confirm-account", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.USER);
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("deny-account", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.USER);
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("account-page", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.USER);
        actionRoleTable.put("transfer-money", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.GUEST);
        signUpRoles.add(Role.USER);
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("set-language", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("deposit", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("withdraw", signUpRoles);


        return new ActionMapAuthorizationManager(actionRoleTable);
    }

    private final ControllerMapper buildControllerMapper() {
        HashMap<String, HashMap<HttpMethod, HttpController>> actionRoleTable = new HashMap<>();
        HashMap<HttpMethod, HttpController> methodMap;

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, signUpController);
        actionRoleTable.put("sign-up", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, logInController);
        actionRoleTable.put("log-in", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, logOutController);
        actionRoleTable.put("log-out", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, registerBankAccountController);
        actionRoleTable.put("register-account", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.GET, listUnconfirmedController);
        actionRoleTable.put("list-unconfirmed", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.GET, listUserAccountsController);
        actionRoleTable.put("list-accounts", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, confirmAccountController);
        actionRoleTable.put("confirm-account", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, denyAccountController);
        actionRoleTable.put("deny-account", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.GET, accountInfoController);
        actionRoleTable.put("account-page", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, transferMoneyController);
        actionRoleTable.put("transfer-money", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, setLanguageController);
        actionRoleTable.put("set-language", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, depositController);
        actionRoleTable.put("deposit", methodMap);

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, withdrawController);
        actionRoleTable.put("withdraw", methodMap);

        return new DefaultControllerMapper(actionRoleTable);
    }

    private final void createFrontController() {

        frontController = new FrontController(
                controllerMapper,
                authorizationManager);

        ServletRegistration.Dynamic regDyn =
                servletContext.addServlet("dispatcher", frontController);
        regDyn.addMapping("/dispatcher");
        regDyn.setLoadOnStartup(1);
    }

    protected void setupScheduled() throws SchedulerException {


        SchedulerFactory sf = new StdSchedulerFactory();
        scheduler = sf.getScheduler();
        scheduler.getContext().put("ScheduledDAO", scheduledDAO);
        JobDetail accumulationJob = newJob(DailyInterestAccumulation.class)
                .withIdentity("DailyInterestAccumulation", "group1")
                .build();


        Trigger daily = newTrigger()
                .withIdentity("daily", "group1")
                .withSchedule(cronSchedule("0 0 1 1/1 * ? *")// Daily 1:00 a. m. "0 0 1 1/1 * ? *"
                        .withMisfireHandlingInstructionFireAndProceed())  // Every minute "0 0/1 * 1/1 * ? *"
                .forJob(accumulationJob)
                .build();

        JobDetail flushJob = newJob(MonthlyInterestFlush.class)
                .withIdentity("MonthlyInterestFlush", "group1")
                .build();


        Trigger monthly = newTrigger()
                .withIdentity("monthly", "group1")
                .withSchedule(cronSchedule("0 0 2 1 1/1 ? *")// Monthly, first day, 2:00 a. m. "0 0 2 1 1/1 ? *"
                        .withMisfireHandlingInstructionFireAndProceed())  //Every 5 minutes, "0 0/5 * 1/1 * ? *"
                .forJob(flushJob)
                .build();

        scheduler.start();
        scheduler.scheduleJob(accumulationJob, daily);

        scheduler.scheduleJob(flushJob, monthly);


    }

    @Override
    public void shutdown() {
        try {
            scheduler.shutdown(true);
            springContext.close();

        } catch (SchedulerException e) {
            logger.log(Level.DEBUG, e.getMessage());
            logger.log(Level.WARN, "Failed to shutdown job scheduler, memory leak possible");
        }
    }
}

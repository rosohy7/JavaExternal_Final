package external.letiuka.init;

import external.letiuka.mvc.controller.HttpMethod;
import external.letiuka.mvc.controller.concrete.LogInController;
import external.letiuka.mvc.controller.concrete.LogOutController;
import external.letiuka.mvc.controller.concrete.RegisterBankAccountController;
import external.letiuka.mvc.controller.mapping.ControllerMapper;
import external.letiuka.mvc.controller.mapping.DefaultControllerMapper;
import external.letiuka.persistence.dal.dao.BankAccountDAO;
import external.letiuka.persistence.dal.dao.TransactionDAO;
import external.letiuka.security.PasswordHasher;
import external.letiuka.security.SHA256HexApacheHasher;
import external.letiuka.security.authorization.AuthorizationManager;
import external.letiuka.security.authorization.ActionMapAuthorizationManager;
import external.letiuka.mvc.controller.FrontController;
import external.letiuka.mvc.controller.HttpController;
import external.letiuka.mvc.controller.concrete.SignUpController;
import external.letiuka.security.Role;
import external.letiuka.persistence.connectionpool.ConnectionPool;
import external.letiuka.persistence.connectionpool.TomcatConnectionPool;
import external.letiuka.persistence.dal.DAOFactory;
import external.letiuka.persistence.dal.DefaultDAOFactory;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.persistence.transaction.DefaultTransactionManager;
import external.letiuka.service.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.*;

public class PlainJavaApplicationBuilder implements ApplicationBuilder {
    private static Logger logger = Logger.getLogger(PlainJavaApplicationBuilder.class);

    ConnectionPool pool = new TomcatConnectionPool();
    TransactionManager transactionManager = new DefaultTransactionManager(pool);

    private DAOFactory daoFac;
    private ServiceFactory serviceFac;

    private UserDAO userDAO;
    private BankAccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    private AuthenticationService authService;
    private BankAccountService accountService;

    private PasswordHasher passwordHasher = new SHA256HexApacheHasher();
    private TimeProvider timeProvider = new RealTimeProvider();
    private InterestRateProvider rateProvider = new ConstantInterestRateProvider(25,15);
    private AccountNumberGenerator numberGenerator;

    private SignUpController signUpController;
    private LogInController logInController;
    private LogOutController logOutController;
    private RegisterBankAccountController registerBankAccountController;

    private ServletContext servletContext;
    private ControllerMapper controllerMapper;
    private AuthorizationManager authorizationManager;
    private FrontController frontController;


    public PlainJavaApplicationBuilder(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public final void build() {
        try {
            daoFac = new DefaultDAOFactory(transactionManager);
            createDAL(daoFac);

            serviceFac = new DefaultServiceFactory(transactionManager);
            createServices(serviceFac);

            createHttpControllers();

            controllerMapper = buildControllerMapper();
            authorizationManager = buildAuthorizationManager();
            createFrontController();
        } catch (Exception e) {
            String message = "Failed to build application object graph.";
            logger.log(Level.FATAL,message);
            throw new Error(message);
        }
    }

    protected void createServices(ServiceFactory serFac){
        numberGenerator=new RandomAccountNumberGenerator(accountDAO);
        authService = serFac.getAuthentificationService(userDAO,passwordHasher);
        accountService = serFac.getBankAccountService(
                timeProvider, rateProvider,
                numberGenerator, userDAO, accountDAO);
    }
    protected void createDAL(DAOFactory daoFac){
        userDAO = daoFac.getUserDAO();
        accountDAO=daoFac.getBankAccountDAO();
        transactionDAO = daoFac.getTransactionDAO();
    }
    protected void createHttpControllers(){
        signUpController = new SignUpController(authService);
        logInController = new LogInController(authService);
        logOutController = new LogOutController();
        registerBankAccountController  = new RegisterBankAccountController(accountService);
    }

    protected AuthorizationManager buildAuthorizationManager() {
        HashMap<String, HashSet<Role>> actionRoleTable = new HashMap<>();
        HashSet<Role> signUpRoles;

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.GUEST);
        actionRoleTable.put("sign-up", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.GUEST);
        actionRoleTable.put("log-in", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.USER);
        signUpRoles.add(Role.ADMIN);
        actionRoleTable.put("log-out", signUpRoles);

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.USER);
        actionRoleTable.put("register-account", signUpRoles);

        return new ActionMapAuthorizationManager(actionRoleTable);
    }

    protected ControllerMapper buildControllerMapper() {
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

        return new DefaultControllerMapper(actionRoleTable);
    }

    protected void createFrontController() {

        frontController = new FrontController(
                controllerMapper,
                authorizationManager);

        ServletRegistration.Dynamic regDyn =
                servletContext.addServlet("dispatcher", frontController);
        regDyn.addMapping("/dispatcher");
        regDyn.setLoadOnStartup(1);
    }
}

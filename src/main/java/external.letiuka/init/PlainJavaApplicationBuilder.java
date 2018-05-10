package external.letiuka.init;

import external.letiuka.MVC.controller.HttpMethod;
import external.letiuka.MVC.controller.mapping.ControllerMapper;
import external.letiuka.MVC.controller.mapping.DefaultControllerMapper;
import external.letiuka.security.authorization.AuthorizationManager;
import external.letiuka.security.authorization.ActionMapAuthorizationManager;
import external.letiuka.MVC.controller.FrontController;
import external.letiuka.MVC.controller.HttpController;
import external.letiuka.MVC.controller.concrete.SignUpController;
import external.letiuka.security.Role;
import external.letiuka.persistence.connectionpool.ConnectionPool;
import external.letiuka.persistence.connectionpool.TomcatConnectionPool;
import external.letiuka.persistence.dal.DAOFactory;
import external.letiuka.persistence.dal.DefaultDAOFactory;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.persistence.transaction.DefaultTransactionManager;
import external.letiuka.service.AuthenticationService;
import external.letiuka.service.DefaultServiceFactory;
import external.letiuka.service.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.*;

public class PlainJavaApplicationBuilder implements ApplicationBuilder {
    private ServletContext servletContext;
    private SignUpController signUpController;
    private ConnectionPool pool;
    private TransactionManager transactionManager;
    private DAOFactory daoFac;
    private UserDAO userDAO;
    private ServiceFactory serFac;
    private AuthenticationService authService;
    private FrontController frontController;
    private HttpController signup;


    public PlainJavaApplicationBuilder(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public final void build() {
        try {
            ConnectionPool pool = new TomcatConnectionPool();
            TransactionManager transactionManager = new DefaultTransactionManager(pool);

            daoFac = new DefaultDAOFactory(transactionManager);
            createDAL(daoFac);

            serFac = new DefaultServiceFactory(transactionManager);
            createServices(serFac);

            createHttpControllers();

            ControllerMapper controllerMapper = buildControllerMapper();
            AuthorizationManager authorizationManager = buildAuthorizationManager();
            createFrontController(controllerMapper, authorizationManager);
        } catch (Exception e) {
            throw new Error("Failed to build application object graph.");
        }
    }

    protected void createServices(ServiceFactory serFac){
        authService = serFac.getAuthentificationService(userDAO);
    }
    protected void createDAL(DAOFactory daoFac){
        userDAO = daoFac.getUserDAO();
    }
    protected void createHttpControllers(){
        signUpController = new SignUpController(authService);
    }

    protected AuthorizationManager buildAuthorizationManager() {
        HashMap<String, HashSet<Role>> actionRoleTable = new HashMap<>();
        HashSet<Role> signUpRoles;

        signUpRoles = new HashSet<>();
        signUpRoles.add(Role.GUEST);
        actionRoleTable.put("sign-up", signUpRoles);

        return new ActionMapAuthorizationManager(actionRoleTable);
    }

    protected ControllerMapper buildControllerMapper() {
        HashMap<String, HashMap<HttpMethod, HttpController>> actionRoleTable = new HashMap<>();
        HashMap<HttpMethod, HttpController> methodMap;

        methodMap = new HashMap<>();
        methodMap.put(HttpMethod.POST, signUpController);
        actionRoleTable.put("sign-up", methodMap);

        return new DefaultControllerMapper(actionRoleTable);
    }

    protected void createFrontController(
            ControllerMapper controllerMapper,
            AuthorizationManager authorizationManager) {

        frontController = new FrontController(
                controllerMapper,
                authorizationManager);

        ServletRegistration.Dynamic regDyn =
                servletContext.addServlet("dispatcher", frontController);
        regDyn.addMapping("/dispatcher");
        regDyn.setLoadOnStartup(1);
    }
}

package external.letiuka.springconfig;

import external.letiuka.modelviewcontroller.controller.FrontController;
import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.controller.HttpMethod;
import external.letiuka.modelviewcontroller.controller.concrete.AccountInfoController;
import external.letiuka.modelviewcontroller.controller.concrete.ConfirmAccountController;
import external.letiuka.modelviewcontroller.controller.concrete.DenyAccountController;
import external.letiuka.modelviewcontroller.controller.concrete.DepositController;
import external.letiuka.modelviewcontroller.controller.concrete.ListUnconfirmedController;
import external.letiuka.modelviewcontroller.controller.concrete.ListUserAccountsController;
import external.letiuka.modelviewcontroller.controller.concrete.LogInController;
import external.letiuka.modelviewcontroller.controller.concrete.LogOutController;
import external.letiuka.modelviewcontroller.controller.concrete.RegisterBankAccountController;
import external.letiuka.modelviewcontroller.controller.concrete.SetLanguageController;
import external.letiuka.modelviewcontroller.controller.concrete.SignUpController;
import external.letiuka.modelviewcontroller.controller.concrete.TransferMoneyController;
import external.letiuka.modelviewcontroller.controller.concrete.WithdrawController;
import external.letiuka.modelviewcontroller.controller.mapping.ControllerMapper;
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
import external.letiuka.security.Role;
import external.letiuka.security.authorization.AuthorizationManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "external.letiuka")
@EnableScheduling
@EnableTransactionManagement
public class RootSpringConfig {

    @Autowired
    private SignUpController signUpController;
    @Autowired
    private LogInController logInController;
    @Autowired
    private LogOutController logOutController;
    @Autowired
    private RegisterBankAccountController registerBankAccountController;
    @Autowired
    private ListUnconfirmedController listUnconfirmedController;
    @Autowired
    private ListUserAccountsController listUserAccountsController;
    @Autowired
    private ConfirmAccountController confirmAccountController;
    @Autowired
    private DenyAccountController denyAccountController;
    @Autowired
    private AccountInfoController accountInfoController;
    @Autowired
    private TransferMoneyController transferMoneyController;
    @Autowired
    private SetLanguageController setLanguageController;
    @Autowired
    private DepositController depositController;
    @Autowired
    private WithdrawController withdrawController;

    @Autowired
    private ControllerMapper controllerMapper;
    @Autowired
    private AuthorizationManager authorizationManager;
    @Autowired
    @Qualifier("servletContext")
    private ServletContext servletContext;


    @Bean(name = "authorizationMap")
    public Map<String, HashSet<Role>> getAuthorizationMap() {
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

        return actionRoleTable;
    }

    @Bean(name = "frontController")
    public FrontController getFrontController() {
        FrontController frontController = new FrontController(
                controllerMapper,
                authorizationManager);

        ServletRegistration.Dynamic regDyn =
                servletContext.addServlet("dispatcher", frontController);
        regDyn.addMapping("/dispatcher");
        regDyn.setLoadOnStartup(1);
        return frontController;
    }

    @Bean(name = "controllerMap")
    public Map<String, HashMap<HttpMethod, HttpController>> getControllerMap() {
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

        return actionRoleTable;
    }

    @Bean(name = "sessionFactory")
    @Autowired
    public SessionFactory getSessionFactory(LocalSessionFactoryBean localSessionFactoryBean){
        return localSessionFactoryBean.getObject();
    }

    @Bean(name = "platformTransactionManager")
    @Autowired
    public PlatformTransactionManager getPlatformTransactionManager(SessionFactory sessionFactory){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        transactionManager.setAutodetectDataSource(false);
        return transactionManager;
    }

    @Bean(name = "localSessionFactoryBean")
    public LocalSessionFactoryBean getLocalSessionFactoryBean(){
        Properties hibProps = new Properties();
        hibProps.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        hibProps.put(Environment.USER, "bankapp");
        hibProps.put(Environment.PASS, "bankapp");
        hibProps.put(Environment.URL, "jdbc:mysql://localhost:3306/bankapp?useSSL=false&characterEncoding=utf8");
        hibProps.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        hibProps.put(Environment.HBM2DDL_AUTO, "none");

        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setHibernateProperties(hibProps);
        sessionFactoryBean.setPackagesToScan("external.letiuka");
        return sessionFactoryBean;
    }
}


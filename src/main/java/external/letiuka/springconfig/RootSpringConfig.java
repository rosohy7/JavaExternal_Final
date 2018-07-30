package external.letiuka.springconfig;

import com.mchange.v2.c3p0.DriverManagerDataSource;
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
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"external.letiuka.lifecycle",
        "external.letiuka.persistence",
        "external.letiuka.security",
        "external.letiuka.service",
        "external.letiuka.springconfig"})
@EnableScheduling

@EnableTransactionManagement
public class RootSpringConfig {

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


    @Bean(name = "sessionFactory")
    @Autowired
    public SessionFactory getSessionFactory(LocalSessionFactoryBean localSessionFactoryBean){
        return localSessionFactoryBean.getObject();
    }

    @Bean
    @Autowired
    @Primary
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory,EntityManagerFactory entityManagerFactoryBean){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        transactionManager.setAutodetectDataSource(false);
        return transactionManager;

//        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//        jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean);
//        return jpaTransactionManager;
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
    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean bean){
        return bean.getObject();
    }

    @Bean
    @Autowired
    @Lazy
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPackagesToScan("external.letiuka.persistence");
        entityManagerFactoryBean.setJpaProperties(jpaProps());
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return entityManagerFactoryBean;
    }

    private Properties jpaProps() {
        Properties jpaProps = new Properties();
        jpaProps.put(org.hibernate.cfg.Environment.DRIVER, "com.mysql.jdbc.Driver");
        jpaProps.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        jpaProps.put(org.hibernate.cfg.Environment.URL, "jdbc:mysql://localhost:3306/bankapp?useSSL=false&characterEncoding=utf8");
        jpaProps.put(org.hibernate.cfg.Environment.USER, "bankapp");
        jpaProps.put(org.hibernate.cfg.Environment.PASS, "bankapp");
        jpaProps.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "none");
        jpaProps.put(org.hibernate.cfg.Environment.SHOW_SQL, true);

        return jpaProps;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager jdbcPlatformTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);

        return transactionManager;
    }
    



    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/bankapp?useSSL=false&characterEncoding=utf8");
        dataSource.setUser("bankapp");
        dataSource.setPassword("bankapp");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        return dataSource;
    }
}


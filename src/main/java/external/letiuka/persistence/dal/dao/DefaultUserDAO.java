package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.persistence.transaction.TransactionException;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.security.Role;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.sql.*;

@Repository
public class DefaultUserDAO implements UserDAO {
    private final TransactionManager manager;
    private static final Logger logger = Logger.getLogger(DefaultUserDAO.class);

    public DefaultUserDAO(TransactionManager manager, SessionFactory sessionFactory) {
        this.manager = manager;
    }


    @Override
    public UserEntity readUser(String login){
        Session session = manager.getSession();
        return (UserEntity) session
                .createQuery("SELECT u FROM User u WHERE u.login = :login")
                .setParameter("login", login)
                .getSingleResult();
    }
}

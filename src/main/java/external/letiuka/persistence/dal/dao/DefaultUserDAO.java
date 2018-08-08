package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.security.Role;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.sql.*;

@Repository
public class DefaultUserDAO implements UserDAO {
    private final SessionFactory sessionFactory;

    private static final Logger logger = Logger.getLogger(DefaultUserDAO.class);

    @Autowired
    public DefaultUserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public UserEntity readUser(String login){
        Session session = sessionFactory.getCurrentSession();
        return (UserEntity) session
                .createQuery("SELECT u FROM User u WHERE u.login = :login")
                .setParameter("login", login)
                .getSingleResult();
    }
}

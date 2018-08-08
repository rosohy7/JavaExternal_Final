package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.entities.UserEntity;
/**
 * Provides interface for accessing users in a database
 */
public interface UserDAO {


    void insertUser(UserEntity user) throws DAOException;
    UserEntity readUser(long id) throws DAOException;
    UserEntity readUser(String login) throws DAOException;
    void updateUser(UserEntity user) throws DAOException;
    void deleteUser(long id) throws DAOException;

}

package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.entities.UserEntity;
/**
 * Provides interface for accessing users in a database
 */
public interface UserDAO {


    UserEntity readUser(String login);

}

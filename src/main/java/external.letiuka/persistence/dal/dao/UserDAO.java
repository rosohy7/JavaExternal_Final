package external.letiuka.persistence.dal.dao;

import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dto.User;

public interface UserDAO {

    void InsertUser(User user) throws DAOException;
}

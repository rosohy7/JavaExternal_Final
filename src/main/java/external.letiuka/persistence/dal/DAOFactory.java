package external.letiuka.persistence.dal;

import external.letiuka.persistence.dal.dao.UserDAO;

public interface DAOFactory {
    UserDAO getUserDAO();
}

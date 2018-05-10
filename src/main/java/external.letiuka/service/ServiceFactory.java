package external.letiuka.service;

import external.letiuka.persistence.dal.dao.UserDAO;

public interface ServiceFactory {
    DefaultAuthenticationService getAuthentificationService(UserDAO userDAO);
}

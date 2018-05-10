package external.letiuka.service;

import external.letiuka.persistence.dto.User;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;
import external.letiuka.security.Role;

public class DefaultAuthenticationService implements AuthenticationService {
    private TransactionManager manager;
    private UserDAO userDAO;

    public DefaultAuthenticationService(TransactionManager manager, UserDAO userDAO) {
        this.manager = manager;
        this.userDAO = userDAO;
    }

    @Override
    public void registerUser(User user) throws ServiceException// TODO
    {
        try {
            user.setRole(Role.USER);
            userDAO.InsertUser(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}

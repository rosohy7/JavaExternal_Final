package external.letiuka.persistence.dal;

import external.letiuka.persistence.dal.dao.DefaultUserDAO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.transaction.TransactionManager;

public class DefaultDAOFactory implements DAOFactory {
    private TransactionManager manager;
    public DefaultDAOFactory(TransactionManager manager){
        this.manager=manager;
    }

    @Override
    public UserDAO getUserDAO() {
        return new DefaultUserDAO(manager);
    }
}

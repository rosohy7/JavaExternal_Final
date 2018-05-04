package external.letiuka.service;

import external.letiuka.dao.ClientDao;
import external.letiuka.dao.DAOFactory;
import external.letiuka.dto.ClientDto;
import external.letiuka.exception.DAOException;
import external.letiuka.exception.TransactionException;
import external.letiuka.exception.ServiceException;
import external.letiuka.transaction.TransactionManager;

public class MyService {
    private final DAOFactory factory = DAOFactory.getInstance();

    public void serviceMethod() throws ServiceException{
        try {
            ClientDao clientDao = factory.getClientDao();
            ClientDto client =new ClientDto();
            try {
                TransactionManager.beginTransaction();
                clientDao.insertClient(client);
                TransactionManager.commit();
            } catch (DAOException e) {
                TransactionManager.rollback();
            }
        } catch (TransactionException e) {
            throw new ServiceException(e);
        }
    }
}

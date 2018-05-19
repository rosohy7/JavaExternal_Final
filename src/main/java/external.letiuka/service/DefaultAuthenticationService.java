package external.letiuka.service;

import external.letiuka.modelviewcontroller.model.dto.UserDTO;
import external.letiuka.persistence.dal.DAOException;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.persistence.transaction.TransactionManager;

import external.letiuka.security.PasswordHasher;
import external.letiuka.security.Role;
import org.apache.log4j.Logger;


public class DefaultAuthenticationService implements AuthenticationService {
    private final TransactionManager manager;
    private final UserDAO userDAO;
    private final PasswordHasher hasher;
    private static final Logger logger = Logger.getLogger(DefaultAuthenticationService.class);

    public DefaultAuthenticationService(TransactionManager manager, UserDAO userDAO, PasswordHasher hasher) {
        this.manager = manager;
        this.userDAO = userDAO;
        this.hasher=hasher;
    }

    @Override
    public void register(UserDTO userDTO) throws ServiceException// TODO
    {
        UserEntity userEntity = new UserEntity();

        userEntity.setLogin(userDTO.getLogin());
        userEntity.setRole(userDTO.getRole());

        userEntity.setPasswordHash(hasher.getHash(userDTO.getPassword()));

        try {
            userDAO.insertUser(userEntity);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Role logIn(UserDTO user) throws ServiceException {
        UserEntity userEntity = new UserEntity();

        try{
            userEntity = userDAO.readUser(user.getLogin());
            String correctHash=userEntity.getPasswordHash();
            if(!correctHash.equals(hasher.getHash(user.getPassword())))
                throw new ServiceException("Wrong password");
            Role role = userEntity.getRole();
            return role;
        } catch(DAOException e)
        {
            throw new ServiceException(e);
        }
    }
}

package external.letiuka.service;

import external.letiuka.modelviewcontroller.model.dto.UserDTO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.entities.UserEntity;
import external.letiuka.persistence.transaction.TransactionManager;

import external.letiuka.security.PasswordHasher;
import external.letiuka.security.Role;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticationService implements AuthenticationService {
    private final TransactionManager manager;
    private final UserDAO userDAO;
    private final PasswordHasher hasher;
    private static final Logger logger = Logger.getLogger(DefaultAuthenticationService.class);

    public DefaultAuthenticationService(TransactionManager manager, UserDAO userDAO, PasswordHasher hasher) {
        this.manager = manager;
        this.userDAO = userDAO;
        this.hasher = hasher;
    }

    @Override
    public void register(UserDTO userDTO) throws ServiceException// TODO
    {
        UserEntity userEntity = new UserEntity();

        userEntity.setLogin(userDTO.getLogin());
        userEntity.setRole(userDTO.getRole());

        userEntity.setPasswordHash(hasher.getHash(userDTO.getPassword()));

        try {
            manager.beginTransaction();
            manager.getSession().save(userEntity);
            manager.commit();
        } catch (Exception e) {

            manager.rollback();

            throw new ServiceException(e);
        }
    }

    @Override
    public Role logIn(UserDTO user) throws ServiceException {
        UserEntity userEntity;

        try {
            manager.beginTransaction();
            userEntity = userDAO.readUser(user.getLogin());
            String correctHash = userEntity.getPasswordHash();
            if (!correctHash.equals(hasher.getHash(user.getPassword())))
                throw new ServiceException("Wrong password");
            Role role = userEntity.getRole();
            manager.commit();
            return role;

        } catch (Exception e) {
            manager.rollback();
            throw new ServiceException(e);
        }
    }
}

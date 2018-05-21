package external.letiuka.service;

import external.letiuka.modelviewcontroller.model.dto.UserDTO;
import external.letiuka.security.Role;

/**
 * Responsible for managing authentication
 */
public interface AuthenticationService {
    /*
    Registers new user with role 'USER' in database
     */
    void register(UserDTO user) throws ServiceException;

    /*
    Checks user credentials and provides user role if login is successful
     */
    Role logIn(UserDTO user) throws ServiceException;
}

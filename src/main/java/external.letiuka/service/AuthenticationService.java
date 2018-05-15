package external.letiuka.service;

import external.letiuka.mvc.model.dto.UserDTO;
import external.letiuka.security.Role;

public interface AuthenticationService {
    void register(UserDTO user) throws ServiceException;
    Role logIn(UserDTO user) throws ServiceException;
}

package external.letiuka.service;

import external.letiuka.persistence.dto.User;

public interface AuthenticationService {
    void registerUser(User user) throws ServiceException;
}

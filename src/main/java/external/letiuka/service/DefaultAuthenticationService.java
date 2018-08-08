package external.letiuka.service;

import external.letiuka.modelviewcontroller.model.dto.UserDTO;
import external.letiuka.persistence.dal.dao.UserDAO;
import external.letiuka.persistence.entities.UserEntity;

import external.letiuka.security.PasswordHasher;
import external.letiuka.security.Role;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;

@Service
public class DefaultAuthenticationService implements AuthenticationService, AuthenticationProvider {
    private final UserDAO userDAO;
    private final PasswordHasher hasher;
    private final SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(DefaultAuthenticationService.class);

    public DefaultAuthenticationService(SessionFactory sessionFactory, UserDAO userDAO, PasswordHasher hasher) {
        this.sessionFactory = sessionFactory;
        this.userDAO = userDAO;
        this.hasher = hasher;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void register(UserDTO userDTO) throws ServiceException// TODO
    {
        UserEntity userEntity = new UserEntity();

        userEntity.setLogin(userDTO.getLogin());
        userEntity.setRole(userDTO.getRole());

        userEntity.setPasswordHash(hasher.getHash(userDTO.getPassword()));
        sessionFactory.getCurrentSession().save(userEntity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Role logIn(UserDTO user) throws ServiceException {
        UserEntity userEntity = userDAO.readUser(user.getLogin());
        String correctHash = userEntity.getPasswordHash();
        if (!correctHash.equals(hasher.getHash(user.getPassword())))
            throw new ServiceException("Wrong password");
        Role role = userEntity.getRole();
        return role;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthentication = (UsernamePasswordAuthenticationToken) authentication;
        String username = usernamePasswordAuthentication.getPrincipal().toString();
        String password = usernamePasswordAuthentication.getCredentials().toString();
        UserDTO user = new UserDTO();
        user.setLogin(username);
        user.setPassword(password);
        Role role;
        try{
            role =logIn(user);
        } catch (ServiceException e){
            throw new AuthenticationCredentialsNotFoundException("Wrong username or password",e);
        }
        usernamePasswordAuthentication.setAuthenticated(true);
        usernamePasswordAuthentication.eraseCredentials();
        return usernamePasswordAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}

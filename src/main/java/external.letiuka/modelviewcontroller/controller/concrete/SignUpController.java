package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.UserDTO;
import external.letiuka.security.Role;
import external.letiuka.service.AuthenticationService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for signing up.
 */
public class SignUpController implements HttpController {
    private final AuthenticationService authService;
    private static final Logger logger = Logger.getLogger(SignUpController.class);

    public SignUpController(AuthenticationService authService) {
        this.authService = authService;
    }


    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        logger.log(Level.TRACE,"Attempting to register user");
        UserDTO user = new UserDTO();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String repassword = req.getParameter("repassword");
        Role role = Role.USER;
        if (!validateLogin(login)) {
            redirectBack(req,resp,"Login should be at least 3 symbols long");
            return;
        }
        if (!validatePassword(password)) {
            redirectBack(req,resp,"Password should be at least 3 symbols long");
            return;
        }
        if (!password.equals(repassword)) {
            redirectBack(req,resp,"Passwords should match");
            return;

        }
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(Role.USER);
        try {
            authService.register(user);
            logger.log(Level.INFO,login + " has signed up.");
        } catch (ServiceException e) {
            redirectBack(req,resp,"Failed to sign-up");
            return;
        }
        try {
            resp.sendRedirect("/bankapp/auth/log-in.jsp");
        } catch (IOException e) {
            logger.log(Level.ERROR,"Could not redirect to log in page after successful registration");
        }
    }

    private boolean validatePassword(String password) {
        if (password == null) return false;
        if(!password.matches("\\w*")) return false;
        if (password.length() < 3) return false;
        return true;
    }

    private boolean validateLogin(String login) {
        if (login == null) return false;
        if(!login.matches("\\w*")) return false;
        if (login.length() < 3) return false;
        return true;
    }
    private void redirectBack(HttpServletRequest req, HttpServletResponse resp, String message){

        logger.log(Level.DEBUG,"User failed to sign-up due to failed validation." +
                " Sent message to user: "+message);
        HttpSession session = req.getSession();
        session.setAttribute("message", message);
        try {
            resp.sendRedirect("/bankapp/auth/sign-up.jsp");
        }
        catch(IOException e){
            logger.log(Level.ERROR,"Could not redirect back to sign up page after failed validation");
            logger.log(Level.DEBUG, e.getStackTrace());
            try {
                resp.sendError(404);
            } catch (IOException e1) {
            }
        }
    }
}

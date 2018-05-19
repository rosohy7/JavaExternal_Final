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

public class LogInController implements HttpController {
    private final AuthenticationService authService;
    private static final Logger logger = Logger.getLogger(LogInController.class);

    public LogInController(AuthenticationService authService) {
        this.authService = authService;
    }


    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        UserDTO user = new UserDTO();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        Role role;
        if (!validateLogin(login)) {
            redirectBack(req, resp, "Login should only include latin symbols, number and underscore");
            return;
        }
        if (!validatePassword(password)) {
            redirectBack(req, resp, "Password should only include latin symbols, number and underscore");
            return;
        }
        user.setLogin(login);
        user.setPassword(password);
        try {
            role = authService.logIn(user);
        } catch (ServiceException e) {
            redirectBack(req, resp, "Wrong login or password");
            return;
        }
        HttpSession session = req.getSession();
        session.setAttribute("role",role.toString());
        session.setAttribute("login",login);
        try {
            resp.sendRedirect("/bankapp/main/home.jsp");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Could not redirect to home page after successful log in");
        }
    }

    private boolean validatePassword(String password) {
        if (password == null) return false;
        if (!password.matches("\\w*")) return false;
        return true;
    }

    private boolean validateLogin(String login) {
        if (login == null) return false;
        if (!login.matches("\\w*")) return false;
        return true;
    }

    private void redirectBack(HttpServletRequest req, HttpServletResponse resp, String message) {

        logger.log(Level.DEBUG, "User failed to log in due to failed input validation." +
                " Sent message to user: " + message);
        HttpSession session = req.getSession();
        session.setAttribute("message", message);
        try {
            resp.sendRedirect("/bankapp/auth/log-in.jsp");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Could not redirect back to log in page after failed input validation");
        }
    }
}

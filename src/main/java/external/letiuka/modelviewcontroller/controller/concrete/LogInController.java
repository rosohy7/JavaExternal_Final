package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.UserDTO;
import external.letiuka.security.Role;
import external.letiuka.service.AuthenticationService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for logging in.
 */
@Controller
public class LogInController implements HttpController {
    private final AuthenticationService authService;
    private static final Logger logger = Logger.getLogger(LogInController.class);

    public LogInController(AuthenticationService authService) {
        this.authService = authService;
    }


    @RequestMapping(value = "dispatcher", params = "action=log-in", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam String login,
                              @RequestParam String password,
                              HttpSession session) {
        ModelAndView mav = new ModelAndView();
        UserDTO user = new UserDTO();
        Role role;
        if (!validateLogin(login)) {
            session.setAttribute("message", "Login should only include latin symbols, number and underscore");
            mav.setViewName("redirect:/auth/log-in.jsp");
            return mav;
        }
        if (!validatePassword(password)) {
            session.setAttribute("message", "Password should only include latin symbols, number and underscore");
            mav.setViewName("redirect:/auth/log-in.jsp");
            return mav;
        }
        user.setLogin(login);
        user.setPassword(password);
        try {
            role = authService.logIn(user);
        } catch (ServiceException e) {
            mav.setStatus(HttpStatus.BAD_REQUEST);
            return mav;
        }
        session.setAttribute("role", role.toString());
        session.setAttribute("login", login);
        mav.setViewName("redirect:/main/home.jsp");
        return mav;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        // stub to keep the interface
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


}

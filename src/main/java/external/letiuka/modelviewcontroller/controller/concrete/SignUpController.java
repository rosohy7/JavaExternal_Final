package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.model.dto.UserDTO;
import external.letiuka.security.Role;
import external.letiuka.service.AuthenticationService;
import external.letiuka.service.ServiceException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller responsible for signing up.
 */
@Controller
public class SignUpController implements HttpController {
    private final AuthenticationService authService;
    private static final Logger logger = Logger.getLogger(SignUpController.class);
    private static final String SIGN_UP_FORM_PATH = "/auth/sign-up.jsp";
    private static final String LOG_IN_FORM_PATH = "/auth/log-in.jsp";

    public SignUpController(AuthenticationService authService) {
        this.authService = authService;
    }


    @RequestMapping(value = "dispatcher",params = "action=sign-up", method = RequestMethod.POST)
    public ModelAndView signUp(@RequestParam String login,
                               @RequestParam String password,
                               @RequestParam String repassword,
                               HttpSession session){
        ModelAndView mav = new ModelAndView();

        if (!validateLogin(login)) {
            session.setAttribute("message","Login should be at least 3 symbols long");
            mav.setViewName("redirect:/"+SIGN_UP_FORM_PATH);
            return mav;
        }
        if (!validatePassword(password)) {
            session.setAttribute("message","Password should be at least 3 symbols long");
            mav.setViewName("redirect:/"+SIGN_UP_FORM_PATH);
            return mav;
        }
        if (!password.equals(repassword)) {
            session.setAttribute("message","Passwords should match");
            mav.setViewName("redirect:/"+SIGN_UP_FORM_PATH);
            return mav;

        }
        UserDTO user = new UserDTO();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(Role.USER);
        try{
            authService.register(user);
        } catch(ServiceException e){
            logger.log(Level.DEBUG,"User failed to sign-up with login " + login,e);
            session.setAttribute("message","Failed to sign up");
            mav.setViewName("redirect:/"+SIGN_UP_FORM_PATH);
            return mav;
        }
        logger.log(Level.INFO,login + " has signed up.");
        mav.setViewName("redirect:/"+LOG_IN_FORM_PATH);
        return mav;
    }
    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        // Stub
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
}

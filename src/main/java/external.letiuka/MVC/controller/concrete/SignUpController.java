package external.letiuka.MVC.controller.concrete;

import external.letiuka.MVC.controller.HttpController;
import external.letiuka.persistence.dto.User;
import external.letiuka.service.AuthenticationService;
import external.letiuka.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpController implements HttpController {
    private AuthenticationService authService;
    public SignUpController(AuthenticationService authService){
        this.authService=authService;
    }


    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        User user=new User();
        try {
            authService.registerUser(user);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        try {
            resp.sendRedirect("/bankapp/home.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

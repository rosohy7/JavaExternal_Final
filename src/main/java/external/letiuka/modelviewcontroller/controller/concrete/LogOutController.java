package external.letiuka.modelviewcontroller.controller.concrete;

import external.letiuka.modelviewcontroller.controller.HttpController;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller responsible for logging out.
 */
@Controller
public class LogOutController  implements HttpController {
    private static final Logger logger = Logger.getLogger(LogOutController.class);

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
        try {
            resp.sendRedirect("/bankapp/");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Could not redirect to index page after log out");
            logger.log(Level.DEBUG, e.getStackTrace());
            try {
                resp.sendError(404);
            } catch (IOException e1) {
            }
        }
    }
}

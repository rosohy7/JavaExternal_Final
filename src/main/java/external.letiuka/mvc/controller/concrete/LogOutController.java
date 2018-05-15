package external.letiuka.mvc.controller.concrete;

import external.letiuka.mvc.controller.HttpController;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogOutController  implements HttpController {
    private static Logger logger = Logger.getLogger(LogOutController.class);

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
        try {
            resp.sendRedirect("/bankapp/");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Could not redirect to index page after log out");
        }
    }
}

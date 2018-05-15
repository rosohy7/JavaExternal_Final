package external.letiuka.mvc.controller;

import external.letiuka.mvc.controller.mapping.ControllerMapper;
import external.letiuka.security.Role;
import external.letiuka.security.authorization.AuthorizationManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public final class FrontController extends HttpServlet {
    private ControllerMapper controllerMapper;
    private AuthorizationManager authorizationManager;
    private static final Logger logger = Logger.getLogger(FrontController.class);

    public FrontController(ControllerMapper controllerMapper, AuthorizationManager authorizationManager) {
        this.controllerMapper = controllerMapper;
        this.authorizationManager = authorizationManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serve(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serve(req, resp);
    }

    private void serve(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        logger.log(Level.TRACE, "Received a request with action = " + action);
        HttpMethod method;
        try {
            method = HttpMethod.valueOf(req.getMethod());
        } catch (IllegalArgumentException e) {
            resp.sendError(405, "This application only supports GET and POST methods.");
            return;
        }

        HttpSession session = req.getSession();

        Role role = (Role) session.getAttribute("role");
        if (role == null) role = Role.GUEST;

        HttpController targetController=null;
        if (authorizationManager.authorize(action, role)) {
            try{
                targetController= controllerMapper.getController(action, method);
            } catch(NullPointerException e) {
                HttpMethod antimethod =
                        (method == HttpMethod.GET) ? HttpMethod.POST : HttpMethod.GET;
                if (controllerMapper.getController(action, antimethod) != null)
                    resp.sendError(405, "Request used "
                            + antimethod.toString() + " method instead of " + method.toString());
                else resp.sendError(404,"There is no such action");
            }
            if(targetController!=null) targetController.invoke(req,resp);
        } else {
            HttpMethod antimethod =
                    (method == HttpMethod.GET) ? HttpMethod.POST : HttpMethod.GET;
            if (controllerMapper.getController(action, method) != null) {
                if (role == Role.GUEST)
                    resp.sendError(401,"Log in to complete this request");
                else
                    resp.sendError(403,"You are not authorized for this request");
            }
        }
    }


}

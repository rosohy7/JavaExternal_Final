package external.letiuka.MVC.controller;

import external.letiuka.MVC.controller.mapping.ControllerMapper;
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
        doAct(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doAct(req, resp);
    }

    private void doAct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.TRACE,"Received a request");
        String action = req.getParameter("action");


        HttpSession session = req.getSession();

        Role role = (Role) session.getAttribute("role");
        if (role == null) role = Role.GUEST;

        HttpMethod method = HttpMethod.valueOf(req.getMethod());

        if (authorizationManager.authorize(action, role)) {
            controllerMapper.getController(action, method).invoke(req, resp);
        } else {
            HttpMethod antimethod = (method == HttpMethod.GET) ? HttpMethod.POST : HttpMethod.GET;
            if (controllerMapper.getController(action, method) != null) {
                if (role == Role.GUEST)
                    resp.sendError(401);
                else
                    resp.sendError(403);
            } else if (controllerMapper.getController(action, antimethod) != null)
                resp.sendError(405);
            else resp.sendError(404);
        }
    }


}

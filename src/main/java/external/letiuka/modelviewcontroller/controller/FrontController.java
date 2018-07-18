package external.letiuka.modelviewcontroller.controller;

import external.letiuka.modelviewcontroller.controller.mapping.ControllerMapper;
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

/**
 * Implements front controller pattern.
 * Responsible for sending requests to regular controllers
 * if the user is authorized for the request.
 * Only supports DO and GET http requests.
 * Should be registered in server container at runtime in lifecycle manager
 */

public final class FrontController extends HttpServlet {
    private final ControllerMapper controllerMapper;
    private final AuthorizationManager authorizationManager;
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
        Role role;
        try {
            role = Role.valueOf((String) session.getAttribute("role"));
        } catch (NullPointerException e) {
            role = Role.GUEST;
        }

        HttpController targetController = null;
        if (authorizationManager.authorize(action, role)) {

            targetController = controllerMapper.getController(action, method);

            if (targetController != null)
                targetController.invoke(req, resp);
            else {

                resp.sendError(405, "Request used wrong http method");

            }

        } else {
            HttpMethod antimethod =
                    (method == HttpMethod.GET) ? HttpMethod.POST : HttpMethod.GET;
            if (controllerMapper.getController(action, method) != null) {
                if (role == Role.GUEST)
                    resp.sendError(401, "Log in to complete this request");
                else
                    resp.sendError(403, "You are not authorized for this request");

            } else
                resp.sendError(404, "There is no such action");


        }
    }


}

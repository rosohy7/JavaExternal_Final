package external.letiuka.modelviewcontroller.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Controller that is called from front controller using command string it is mapped to.
 */
public interface HttpController {
    void invoke(HttpServletRequest req, HttpServletResponse resp);
}

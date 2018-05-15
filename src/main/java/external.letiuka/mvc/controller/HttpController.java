package external.letiuka.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpController {
    void invoke(HttpServletRequest req, HttpServletResponse resp);
}

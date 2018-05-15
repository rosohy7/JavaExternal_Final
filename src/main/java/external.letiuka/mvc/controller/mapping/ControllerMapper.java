package external.letiuka.mvc.controller.mapping;

import external.letiuka.mvc.controller.HttpController;
import external.letiuka.mvc.controller.HttpMethod;

public interface ControllerMapper {
    HttpController getController(String action, HttpMethod method);
}

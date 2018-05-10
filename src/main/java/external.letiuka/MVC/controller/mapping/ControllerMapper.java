package external.letiuka.MVC.controller.mapping;

import external.letiuka.MVC.controller.HttpController;
import external.letiuka.MVC.controller.HttpMethod;

public interface ControllerMapper {
    HttpController getController(String action, HttpMethod method);
}

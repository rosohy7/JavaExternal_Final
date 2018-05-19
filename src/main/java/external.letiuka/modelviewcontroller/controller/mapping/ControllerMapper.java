package external.letiuka.modelviewcontroller.controller.mapping;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.controller.HttpMethod;

public interface ControllerMapper {
    HttpController getController(String action, HttpMethod method);
}

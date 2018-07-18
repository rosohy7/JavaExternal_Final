package external.letiuka.modelviewcontroller.controller.mapping;

import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.controller.HttpMethod;

/**
 * Responsible for mapping controllers to front controller string commands.
 */
public interface ControllerMapper {
    /*
    Returns controller mapped to command string @action using http-method @method
     */
    HttpController getController(String action, HttpMethod method);
}

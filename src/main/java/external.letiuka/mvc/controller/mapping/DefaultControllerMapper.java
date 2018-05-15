package external.letiuka.mvc.controller.mapping;


import external.letiuka.mvc.controller.HttpController;
import external.letiuka.mvc.controller.HttpMethod;

import java.util.Collections;
import java.util.Map;

public class DefaultControllerMapper implements ControllerMapper {
    Map<String, ? extends Map<HttpMethod, HttpController>> controllerMap;

    public DefaultControllerMapper(Map<String, ? extends Map<HttpMethod, HttpController>> controllerMap) {
        this.controllerMap = Collections.unmodifiableMap(controllerMap);
    }

    @Override
    public HttpController getController(String action, HttpMethod method) {
        try {
            return controllerMap.get(action).get(method);
        } catch (Exception e) {
            return null;
        }
    }
}

package external.letiuka.modelviewcontroller.controller.mapping;


import external.letiuka.modelviewcontroller.controller.HttpController;
import external.letiuka.modelviewcontroller.controller.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

@Component
public class DefaultControllerMapper implements ControllerMapper {

    private final Map<String, ? extends Map<HttpMethod, HttpController>> controllerMap;

    public DefaultControllerMapper(
            @Qualifier("controllerMap")
                    Map<String, ? extends Map<HttpMethod, HttpController>> controllerMap) {
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

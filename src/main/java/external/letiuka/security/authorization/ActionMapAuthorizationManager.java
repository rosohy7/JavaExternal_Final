package external.letiuka.security.authorization;

import external.letiuka.security.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Component
public final class ActionMapAuthorizationManager implements AuthorizationManager {
    private final Map<String,? extends Set<Role>> actionRoleTable;

    public ActionMapAuthorizationManager(
            @Qualifier("authorizationMap") Map<String, ? extends Set<Role>> actionRoleTable) {
        this.actionRoleTable = Collections.unmodifiableMap(actionRoleTable);
    }

    @Override
    public boolean authorize(String action, Role role) {
        try{
            return actionRoleTable.get(action).contains(role);
        } catch(Exception e){
            return false;
        }
    }
}

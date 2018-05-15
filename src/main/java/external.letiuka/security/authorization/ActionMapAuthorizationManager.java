package external.letiuka.security.authorization;

import external.letiuka.security.Role;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class ActionMapAuthorizationManager implements AuthorizationManager {
    Map<String,? extends Set<Role>> actionRoleTable;

    public ActionMapAuthorizationManager(Map<String, ? extends Set<Role>> actionRoleTable) {
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

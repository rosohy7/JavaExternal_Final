package external.letiuka.security.authorization;

import external.letiuka.security.Role;

public interface AuthorizationManager {
    boolean authorize(String action, Role role);
}

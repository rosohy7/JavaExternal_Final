package external.letiuka.service;

import external.letiuka.security.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

public class BankappAuthentication implements Authentication {
    private Role principal;
    private String password;
    private boolean authenticated;


    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Role getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        authenticated=b;
    }

    @Override
    public String getName() {
        return null;
    }
}

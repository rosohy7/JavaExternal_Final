package external.letiuka.modelviewcontroller.model.dto;

import external.letiuka.security.Role;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String login;
    private String password;
    private Role role;

    public UserDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

package external.letiuka.persistence.entities;

import external.letiuka.security.Role;

/**
 * Responsible for user table in a database.
 */
public class UserEntity extends BaseEntity{
    protected Role role;
    protected String login;
    protected String passwordHash;

    public UserEntity() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}

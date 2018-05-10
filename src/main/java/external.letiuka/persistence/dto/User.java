package external.letiuka.persistence.dto;

import external.letiuka.security.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User implements Serializable {
    private int id;
    private Role role;
    private List<BankAccount> bankAccounts;
    private Credentials credentials;
    private AuthenticationData authData;

    public User() {
        bankAccounts = new ArrayList<BankAccount>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role= role;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public AuthenticationData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthenticationData authData) {
        this.authData = authData;
    }
}

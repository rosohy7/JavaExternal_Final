package external.letiuka.security;

public interface PasswordHasher {
    String getHash(String password);
}

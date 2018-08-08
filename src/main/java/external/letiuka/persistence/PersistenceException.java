package external.letiuka.persistence;

public class PersistenceException extends Exception {
    public PersistenceException() {
        super();
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable e) {
        super(e);
    }
}

package external.letiuka.persistence.dal;


public class DAOException extends Exception {

    public DAOException(Throwable e) {
        super(e);
    }

    public DAOException() {
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}

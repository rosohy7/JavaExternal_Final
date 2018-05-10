package external.letiuka.persistence.dal;


import external.letiuka.persistence.PersistenceException;

public class DAOException extends PersistenceException {

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

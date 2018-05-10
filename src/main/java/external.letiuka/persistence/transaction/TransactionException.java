package external.letiuka.persistence.transaction;

import external.letiuka.persistence.PersistenceException;

public class TransactionException extends PersistenceException {
    public TransactionException() {
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable e) {
        super(e);
    }
}

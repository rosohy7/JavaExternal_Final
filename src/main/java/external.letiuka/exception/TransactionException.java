package external.letiuka.exception;

public class TransactionException extends PersistenceException {

    public TransactionException() {
    }
    public TransactionException(Throwable e) {
        super(e);
    }
}

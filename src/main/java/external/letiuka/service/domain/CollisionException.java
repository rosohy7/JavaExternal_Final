package external.letiuka.service.domain;

public class CollisionException extends RuntimeException {
    public CollisionException() {
        super();
    }

    public CollisionException(String message) {
        super(message);
    }

    public CollisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollisionException(Throwable cause) {
        super(cause);
    }

    protected CollisionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

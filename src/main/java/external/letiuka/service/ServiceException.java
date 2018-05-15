package external.letiuka.service;

public class ServiceException extends Exception {
    public ServiceException(Throwable e) {
        super(e);
    }
    public ServiceException() {
        super();

    }
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}

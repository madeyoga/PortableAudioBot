package exceptions;

public class NullTokenException extends Exception {
    public NullTokenException(String message) {
        super(message);
    }

    public NullTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullTokenException(Throwable cause) {
        super(cause);
    }

    public NullTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

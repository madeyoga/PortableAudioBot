package exceptions;

public class DuplicateCommandException extends Exception {
    public DuplicateCommandException(String message) {
        super(message);
    }

    public DuplicateCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateCommandException(Throwable cause) {
        super(cause);
    }

    public DuplicateCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package base.either;

public class BadEitherAccessException extends RuntimeException {
    public BadEitherAccessException(final String message) {
        super(message);
    }

    public BadEitherAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BadEitherAccessException(final Throwable cause) {
        super(cause);
    }
}


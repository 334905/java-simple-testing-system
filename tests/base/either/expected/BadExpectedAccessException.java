package base.either.expected;

import base.either.BadEitherAccessException;

public class BadExpectedAccessException extends BadEitherAccessException {
    public BadExpectedAccessException(final String message) {
        super(message);
    }

    public BadExpectedAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BadExpectedAccessException(final Throwable cause) {
        super(cause);
    }
}

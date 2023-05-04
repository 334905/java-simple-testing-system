package base.either.expected;

import java.util.function.Function;

public abstract sealed class Expected<T, E extends Exception> {
    public abstract boolean hasValue();
    public abstract T getValue();
    public abstract E getError();

    public <U> Expected<U, E> map(final Function<T, U> mapping) {
        if (hasValue()) {
            return Expected.ofValue(mapping.apply(getValue()));
        } else {
            return Expected.ofError(getError());
        }
    }
    public  T getValueOr(final T defaultValue) {
        return hasValue() ? getValue() : defaultValue;
    }
    public <E1 extends Error> T getValueOrThrow(final E1 error) throws E1 {
        if (hasValue()) {
            return getValue();
        } else {
            throw error;
        }
    }

    public <E1 extends Error> T getValueOrApplyThrow(final Function<E, E1> errorFunction) throws E1 {
        if (hasValue()) {
            return getValue();
        } else {
            throw errorFunction.apply(getError());
        }
    }

    public static <T, E extends Exception> Expected<T, E> ofValue(final T value) {
        return new ExpectedValue<>(value);
    }

    public static <T, E extends Exception> Expected<T, E> ofError(final E error) {
        return new ExpectedException<>(error);
    }
    public static <T> Expected<T, Exception> ofError(final Throwable throwable) {
        if (throwable instanceof Exception exception) {
            return Expected.ofError(exception);
        } else {
            throw (Error) throwable;
        }
    }

    private static final class ExpectedException<T, E extends Exception> extends Expected<T, E> {
        private final E error;

        public ExpectedException(E error) {
            this.error = error;
        }

        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public T getValue() {
            throw new BadExpectedAccessException("Trying to get value while holding error", error);
        }

        @Override
        public E getError() {
            return error;
        }
    }

    private static final class ExpectedValue<T, E extends Exception> extends Expected<T, E> {
        private final T value;

        public ExpectedValue(T value) {
            this.value = value;
        }

        @Override
        public boolean hasValue() {
            return true;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public E getError() {
            throw new BadExpectedAccessException("Trying to get error while holding value " + value);
        }
    }
}

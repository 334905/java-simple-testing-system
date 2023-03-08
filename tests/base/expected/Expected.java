package base.expected;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Expected<T, E extends Exception> {
    boolean hasValue();
    T getValue();
    E getError();

    default <U> Expected<U, E> map(final Function<T, U> mapping) {
        if (hasValue()) {
            return Expected.ofValue(mapping.apply(getValue()));
        } else {
            return Expected.ofError(getError());
        }
    }
    default T getValueOr(final T defaultValue) {
        return hasValue() ? getValue() : defaultValue;
    }
    default <E1 extends Error> T getValueOrThrow(final E1 error) throws E1 {
        if (hasValue()) {
            return getValue();
        } else {
            throw error;
        }
    }

    default <E1 extends Error> T getValueOrApplyThrow(final Function<E, E1> errorFunction) throws E1 {
        if (hasValue()) {
            return getValue();
        } else {
            throw errorFunction.apply(getError());
        }
    }

    static <T, E extends Exception> Expected<T, E> ofValue(final T value) {
        return new Expected<>() {
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
        };
    }

    static <T, E extends Exception> Expected<T, E> ofError(final E error) {
        return new Expected<>() {
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
        };
    }
    static <T> Expected<T, Exception> ofError(final Throwable throwable) {
        if (throwable instanceof Exception exception) {
            return Expected.ofError(exception);
        } else {
            throw (Error) throwable;
        }
    }
}

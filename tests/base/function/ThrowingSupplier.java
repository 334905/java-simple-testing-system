package base.function;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;

    default Supplier<T> unchecked() {
        return () -> {
            try {
                return get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}

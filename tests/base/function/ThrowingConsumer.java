package base.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(final T t) throws E;

    default Consumer<T> unchecked() {
        return t -> {
            try {
                accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}

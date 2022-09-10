package base.function;

@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;

    default Runnable unchecked() {
        return () -> {
            try {
                run();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}

package base.pairs;

public class IterableUtils {
    private IterableUtils() {}

    public static <F, S> Iterable<Pair<F, S>> zip(final Iterable<F> first, final Iterable<S> second) {
        return () -> new PairIterator<>(first.iterator(), second.iterator());
    }
}

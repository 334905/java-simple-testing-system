package base.pairs;

public record Pair<F, S>(F first, S second) {
    public static <F, S> Pair<F, S> of(final F first, final S second) {
        return new Pair<>(first, second);
    }

    public static <T> Pair<T, T> duplicate(final T value) {
        return new Pair<>(value, value);
    }
}

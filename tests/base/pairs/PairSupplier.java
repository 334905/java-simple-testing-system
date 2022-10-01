package base.pairs;

import java.util.function.Supplier;

public class PairSupplier<T1, T2> implements Supplier<Pair<T1, T2>> {
    private final Supplier<? extends T1> first;
    private final Supplier<? extends T2> second;

    public PairSupplier(final Supplier<? extends T1> first, final Supplier<? extends T2> second) {
        this.first = first;
        this.second = second;
    }

    public static <T1, T2> PairSupplier<T1, T2> of(final Supplier<? extends T1> first,
                                                   final Supplier<? extends T2> second) {
        return new PairSupplier<>(first, second);
    }

    @Override
    public Pair<T1, T2> get() {
        return Pair.of(first.get(), second.get());
    }
}

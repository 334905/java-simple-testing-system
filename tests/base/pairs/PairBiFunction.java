package base.pairs;

import java.util.function.BiFunction;

public class PairBiFunction<T1, T2, U1, U2, R1, R2> implements BiFunction<Pair<T1, T2>, Pair<U1, U2>, Pair<R1, R2>> {
    private final BiFunction<? super T1, ? super U1, ? extends R1> first;
    private final BiFunction<? super T2, ? super U2, ? extends R2> second;

    public PairBiFunction(final BiFunction<? super T1, ? super U1, ? extends R1> first,
                          final BiFunction<? super T2, ? super U2, ? extends R2> second) {
        this.first = first;
        this.second = second;
    }

    public static <T1, T2, U1, U2, R1, R2>
    PairBiFunction<T1, T2, U1, U2, R1, R2> of(final BiFunction<? super T1, ? super U1, ? extends R1> first,
                                              final BiFunction<? super T2, ? super U2, ? extends R2> second) {
        return new PairBiFunction<>(first, second);
    }

    @Override
    public Pair<R1, R2> apply(final Pair<T1, T2> left, Pair<U1, U2> right) {
        return Pair.of(
                first.apply(left.first(), right.first()),
                second.apply(left.second(), right.second())
        );
    }
}

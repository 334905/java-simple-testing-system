package base.pairs;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PairFunction<T1, T2, R1, R2> implements Function<Pair<T1, T2>, Pair<R1, R2>> {
    private final Function<? super T1, ? extends R1> first;
    private final Function<? super T2, ? extends R2> second;

    public PairFunction(final Function<? super T1, ? extends R1> first,
                        final Function<? super T2, ? extends R2> second) {
        this.first = first;
        this.second = second;
    }

    public static <T1, T2, R1, R2>
    PairFunction<T1, T2, R1, R2> of(final Function<? super T1, ? extends R1> first,
                                    final Function<? super T2, ? extends R2> second) {
        return new PairFunction<>(first, second);
    }

    @Override
    public Pair<R1, R2> apply(final Pair<T1, T2> arg) {
        return Pair.of(first.apply(arg.first()), second.apply(arg.second()));
    }
}

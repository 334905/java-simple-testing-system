package base.pairs;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class DualCollector<T, A1, A2, R1, R2> implements Collector<T, Pair<A1, A2>, Pair<R1, R2>> {
    final private Collector<? super T, A1, ? extends R1> first;
    final private Collector<? super T, A2, ? extends R2> second;

    public DualCollector(final Collector<? super T, A1, ? extends R1> first,
                         final Collector<? super T, A2, ? extends R2> second) {
        this.first = first;
        this.second = second;
    }

    public static <T, A1, A2, R1, R2>
    DualCollector<T, A1, A2, R1, R2> of(final Collector<? super T, A1, ? extends R1> first,
                                        final Collector<? super T, A2, ? extends R2> second) {
        return new DualCollector<>(first, second);
    }

    @Override
    public Supplier<Pair<A1, A2>> supplier() {
        return PairSupplier.of(first.supplier(), second.supplier());
    }

    @Override
    public BiConsumer<Pair<A1, A2>, T> accumulator() {
        final BiConsumer<A1, ? super T> firstAccumulator = first.accumulator();
        final BiConsumer<A2, ? super T> secondAccumulator = second.accumulator();
        return (a, t) -> {
            firstAccumulator.accept(a.first(), t);
            secondAccumulator.accept(a.second(), t);
        };
    }

    @Override
    public BinaryOperator<Pair<A1, A2>> combiner() {
        return PairBinaryOperator.of(first.combiner(), second.combiner());
    }

    @Override
    public Function<Pair<A1, A2>, Pair<R1, R2>> finisher() {
        return PairFunction.of(first.finisher(), second.finisher());
    }

    @Override
    public Set<Characteristics> characteristics() {
        Set<Characteristics> characteristics = EnumSet.noneOf(Characteristics.class);
        characteristics.addAll(first.characteristics());
        characteristics.retainAll(second.characteristics());
        return Collections.unmodifiableSet(characteristics);
    }
}

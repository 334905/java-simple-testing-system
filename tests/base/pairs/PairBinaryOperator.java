package base.pairs;

import java.util.function.BinaryOperator;

public class PairBinaryOperator<T1, T2> extends PairBiFunction<T1, T2, T1, T2, T1, T2> implements BinaryOperator<Pair<T1, T2>> {
    public PairBinaryOperator(final BinaryOperator<T1> first, final BinaryOperator<T2> second) {
        super(first, second);
    }

    public static <T1, T2>
    PairBinaryOperator<T1, T2> of(final BinaryOperator<T1> first, final BinaryOperator<T2> second) {
        return new PairBinaryOperator<>(first, second);
    }
}

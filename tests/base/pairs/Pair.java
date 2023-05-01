package base.pairs;

import base.Asserts;

import java.util.function.BiFunction;

public record Pair<F, S>(F first, S second) {
    public static <F, S> Pair<F, S> of(final F first, final S second) {
        return new Pair<>(first, second);
    }

    public static <T> Pair<T, T> duplicate(final T value) {
        return new Pair<>(value, value);
    }

    public void testEqualsHashCode(final Pair<?, ?> other, final BiFunction<Object, Object, String> pairToString) {
        System.out.println("Testing equality of " + pairToString.apply(first(), second()) + " and " + pairToString.apply(other.first(), other.second));

        final boolean actualEquals = first().equals(other.first());
        final boolean expectedEquals = second().equals(other.second());
        System.out.println("actuals' equals returned " + actualEquals + ", expecteds' equals returned " + expectedEquals);
        Asserts.assertIdentical(
                actualEquals, expectedEquals,
                "actuals' equals returned " + actualEquals + ", expecteds' equals returned " + expectedEquals
        );

        final int thisActualHashCode = first().hashCode();
        final int otherActualHashCode = other.first().hashCode();
        if (expectedEquals) {
            System.out.println("Testing hashCode's equality of " + first() + " and " + other.first());
            Asserts.assertIdentical(
                    thisActualHashCode, otherActualHashCode,
                    "Expected " + first() + " and " + other.first() + "hashCodes to be equal," +
                            " but found " + thisActualHashCode + " and " + otherActualHashCode
            );
        } else {
            System.out.println("Testing hashCode's NON-equality of " + first() + " and " + other.first());
            Asserts.assertNotIdentical(
                    thisActualHashCode, otherActualHashCode,
                    "Expected " + first() + " and " + other.first() + "hashCodes to be not equal," +
                            " but found " + thisActualHashCode + " and " + otherActualHashCode
            );
        }
    }
}

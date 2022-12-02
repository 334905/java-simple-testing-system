package int_n;

public class IntNTestBase {
    protected static void expectTrue(final boolean cond, final String message) {
        if (!cond)
            throw new AssertionError(message);
    }

    protected static void expectFalse(final boolean cond, final String message) {
        expectTrue(!cond, message);
    }

    protected static void expectEqual(final IntNTester tester,
                                    final Object first, final Object second,
                                    final String firstRepr, final String secondRepr)
            throws IllegalAccessException {
        expectTrue(tester.compare(first, second) == 0,
                "Expected \"" + firstRepr + " == " + secondRepr + "\"");
    }

    protected static void expectLess(final IntNTester tester,
                                   final Object first, final Object second,
                                   final String firstRepr, final String secondRepr)
            throws IllegalAccessException {
        expectTrue(tester.compare(first, second) < 0, "Expected \"" + firstRepr + " < " + secondRepr + "\"");
    }

    protected static void expectGreater(final IntNTester tester,
                                      final Object first, final Object second,
                                      final String firstRepr, final String secondRepr)
            throws IllegalAccessException {
        expectTrue(tester.compare(first, second) > 0, "Expected \"" + firstRepr + " > " + secondRepr + "\"");
    }

    protected static void expectEqual(final IntNTester tester,
                                    final short first, final short second,
                                    final String firstRepr, final String secondRepr)
            throws IllegalAccessException, InstantiationException {
        expectEqual(tester, tester.create(first), tester.create(second), firstRepr, secondRepr);
    }

    protected static void expectLess(final IntNTester tester,
                                   final short first, final short second,
                                   final String firstRepr, final String secondRepr)
            throws IllegalAccessException, InstantiationException {
        expectLess(tester, tester.create(first), tester.create(second), firstRepr, secondRepr);
    }

    protected static void expectGreater(final IntNTester tester,
                                      final short first, final short second,
                                      final String firstRepr, final String secondRepr)
            throws IllegalAccessException, InstantiationException {
        expectGreater(tester, tester.create(first), tester.create(second), firstRepr, secondRepr);
    }
}

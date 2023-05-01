package collections;

import base.Asserts;
import base.pairs.Pair;

import java.lang.ref.WeakReference;
import java.util.*;

public class PreSetTest {
    public static void main(String[] args) {
        new PreSetTest().main();
    }

    protected static String actualExpectedToString(final Object actual, final Object expected) {
        return "{actual=" + actual + "; expected=" + expected + "}";
    }

    protected void checkContainsSameAddon(final Pair<? extends PreSet, ? extends Set<TestClass>> testingPair) {
    }

    protected final void checkContainsSame(final Pair<? extends PreSet, ? extends Set<TestClass>> testingPair) {
        final PreSet actual = testingPair.first();
        final Set<TestClass> expected = testingPair.second();
        System.out.println("Testing elements equality of " + actualExpectedToString(actual, expected));

        Asserts.assertIdentical(
                actual.size(), expected.size(),
                "Expected " + actual + ".size() [" + actual.size() + "] to be equal to " + expected + ".size() [" + expected.size() + "]"
        );

        Asserts.assertIdentical(
                actual.isEmpty(), expected.isEmpty(),
                "Expected " + actual + ".isEmpty() [" + actual.isEmpty() + "] to be equal to " + expected + ".isEmpty() [" + expected.isEmpty() + "]"
        );

        {
            final SortedSet<TestClass> actual1 = new TreeSet<>(TestClass.CASE_SENSITIVE_COMPARATOR);
            for (final TestClass testClass : actual) {
                actual1.add(testClass);
            }
            final SortedSet<TestClass> expected1 = new TreeSet<>(TestClass.CASE_SENSITIVE_COMPARATOR);
            expected1.addAll(expected);

            System.out.println("Testing equality of " + actual1 + " and " + expected1 + " (they are sorted versions of " + actual + " and " + expected + " respectively)");

            for (final Iterator<TestClass> expectedIt = expected1.iterator(), actualIt = actual1.iterator(); expectedIt.hasNext(); ) {
                Asserts.assertIdentical(
                        expectedIt.next(), actualIt.next(),
                        actual1 + " is expected to be equal to " + expected1
                );
            }
        }

        for (final TestClass testClass : expected) {
            Asserts.assertTrue(
                    actual.contains(testClass),
                    "Expected " + actual + ".contains(" + testClass + ") to be true"
            );
        }

        checkContainsSameAddon(testingPair);
    }

    protected final void checkContains(final Pair<? extends PreSet, ? extends Set<TestClass>> testingPair, final Collection<?> containsCheck) {
        final PreSet actual = testingPair.first();
        final Set<TestClass> expected = testingPair.second();
        System.out.println("Checking " + actualExpectedToString(actual, expected) + " containing (ot not) " + containsCheck);
        for (final Object testClass : containsCheck) {
            final boolean actualContains = actual.contains(testClass);
            final boolean expectedContains = expected.contains(testClass);
            Asserts.assertIdentical(
                    actualContains, expectedContains,
                    "Expected " + actual + ".contains(" + testClass + ") [" + actualContains + "] to be equal to " +
                            expected + ".contains(" + testClass + ") [" + expectedContains + "]"
            );
        }
    }

    protected final void checkClear(final Pair<? extends PreSet, ? extends Set<TestClass>> testingPair) {
        System.out.println("Testing clear of " + actualExpectedToString(testingPair.first(), testingPair.second()));
        final HashSet<WeakReference<TestClass>> removedReferences = new HashSet<>();
        testingPair.second().stream().map(WeakReference::new).forEach(removedReferences::add);

        testingPair.first().clear();
        testingPair.second().clear();
        checkContainsSame(testingPair);

        System.gc();
        try {
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            throw new AssertionError(e);
        }
        System.gc();

        System.out.println("Checking elements non-existence");
        final List<TestClass> existing = removedReferences.stream()
                .map(WeakReference::get)
                .filter(Objects::nonNull)
                .toList();
        Asserts.assertEmpty(existing, "Expected removed elements not to exist, but the following exists: " + existing);
    }

    protected final void checkAdd(final Pair<? extends PreSet, ? extends Set<TestClass>> testingPair, Collection<? extends TestClass> toAdd) {
        final PreSet actual = testingPair.first();
        final Set<TestClass> expected = testingPair.second();

        System.out.println("Testing adding " + toAdd + " to " + actualExpectedToString(actual, expected));
        for (final TestClass testClass : toAdd) {
            final String actualRepr = actual.toString();
            final String expectedRepr = expected.toString();
            System.out.println(actualRepr + ".add(" + testClass + "), " + expectedRepr + ".add(" + testClass + ")");

            boolean actualResult = actual.add(testClass);
            boolean expectedResult = expected.add(testClass);
            System.out.println("Adding result is " + actualExpectedToString(actual, expected));
            Asserts.assertIdentical(
                    actualResult, expectedResult,
                    "Expected " + actualRepr + ".add(" + testClass + ") [" + actualResult + "], " +
                            "to be equals " + expectedRepr + ".add(" + testClass + ") [" + expectedResult + "]"
            );
        }
        checkContainsSame(testingPair);
    }

    protected final void checkRemove(final Pair<? extends PreSet, ? extends Set<TestClass>> testingPair, Collection<?> toRemove) {
        final PreSet actual = testingPair.first();
        final Set<TestClass> expected = testingPair.second();

        final HashSet<WeakReference<TestClass>> removedReferences = new HashSet<>();
        expected.stream().filter(toRemove::contains).map(WeakReference::new).forEach(removedReferences::add);

        System.out.println("Testing removing " + toRemove + " from " + actualExpectedToString(actual, expected));
        for (final Object testClass : toRemove) {
            final String actualRepr = actual.toString();
            final String expectedRepr = expected.toString();
            System.out.println(actualRepr + ".remove(" + testClass + "), " + expectedRepr + ".remove(" + testClass + ")");

            boolean actualResult = actual.remove(testClass);
            boolean expectedResult = expected.remove(testClass);
            System.out.println("Removing result is " + actualExpectedToString(actual, expected));
            Asserts.assertIdentical(
                    actualResult, expectedResult,
                    "Expected " + actualRepr + ".remove(" + testClass + ") [" + actualResult + "], " +
                            "to be equals " + expectedRepr + ".remove(" + testClass + ") [" + expectedResult + "]"
            );
        }
        checkContainsSame(testingPair);

        System.gc();
        try {
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            throw new AssertionError(e);
        }
        System.gc();

        System.out.println("Checking elements non-existence");
        final List<TestClass> existing = removedReferences.stream()
                .map(WeakReference::get)
                .filter(Objects::nonNull)
                .toList();
        Asserts.assertEmpty(existing, "Expected removed elements not to exist, but the following exists: " + existing);
    }

    protected final void checkIteratorRemove(final Pair<? extends PreSet, ? extends Set<TestClass>> testingPair, Set<?> toRemove) {
        final PreSet actual = testingPair.first();
        final Set<TestClass> expected = testingPair.second();

        final HashSet<WeakReference<TestClass>> removedReferences = new HashSet<>();
        expected.stream().filter(toRemove::contains).map(WeakReference::new).forEach(removedReferences::add);

        System.out.println("Testing removing " + toRemove + " from " + actualExpectedToString(actual, expected) + " using iterators");
        for (final Iterator<TestClass> actualIt = actual.iterator(), expectedIt = expected.iterator(); expectedIt.hasNext();) {
            final TestClass actualItem = actualIt.next();
            if (toRemove.contains(actualItem)) {
                actualIt.remove();
            }
            final TestClass expectedItem = expectedIt.next();
            if (toRemove.contains(expectedItem)) {
                expectedIt.remove();
            }
        }
        checkContainsSame(testingPair);

        System.gc();
        try {
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            throw new AssertionError(e);
        }
        System.gc();

        System.out.println("Checking elements non-existence");
        final List<TestClass> existing = removedReferences.stream()
                .map(WeakReference::get)
                .filter(Objects::nonNull)
                .toList();
        Asserts.assertEmpty(existing, "Expected removed elements not to exist, but the following exists: " + existing);
    }

    public void main() {
        final Pair<PreSet, Set<TestClass>> testingPair = Pair.of(new HashPreSet(), new HashSet<>());
        checkContainsSame(testingPair);
        checkAdd(testingPair, Arrays.asList(new TestClass("a"), null, new TestClassDerived("b"), null, new TestClassDerived("a"), new TestClass("b")));
        checkContains(testingPair, Arrays.asList(new TestClass("a"), null, new Object(), "a", new TestClassDerived("b"), new TestClassDerived("a"), new TestClass("b"), new TestClass("c"), new TestClassDerived("c")));
        checkClear(testingPair);
        checkRemove(testingPair, Arrays.asList(null, new TestClassBase("a"), new TestClassBase("b"), new Object(), "b"));
        checkAdd(testingPair, Arrays.asList(new TestClassDerived("a"), new TestClassDerived("b"), new TestClass("c"), new TestClass("d")));
        checkContains(testingPair, Arrays.asList(null, new TestClassBase("a"), new TestClassBase("b"), new TestClass("a"), new TestClass("b"), new Object(), "a"));
        checkIteratorRemove(testingPair, new HashSet<>(Arrays.asList(null, new TestClassBase("c"), new TestClass("b"), new Object(), "a", "b")));
        checkAdd(testingPair, Arrays.asList(new TestClassDerived("?"), new TestClass("#"), new TestClassDerived("!")));
        checkRemove(testingPair, Arrays.asList(null, new TestClassBase("c"), new TestClass("?"), new TestClassBase("!"), new TestClassBase("a"), new Object()));
    }
}

package collections;

import base.Asserts;
import base.pairs.IterableUtils;
import base.pairs.Pair;

import java.util.*;

public class TreePreSetTest extends PreSetTest {
    @Override
    protected void checkContainsSameAddon(Pair<? extends PreSet, ? extends Set<TestClass>> testingPair) {
        for (final Pair<TestClass, TestClass> pair : IterableUtils.zip(testingPair.first(), testingPair.second())) {
            Asserts.assertIdentical(
                    pair.first(), pair.second(),
                    "Expected TreeSet to be sorted"
            );
        }
    }

    public void main(final Pair<? extends PreSet, ? extends TreeSet<TestClass>> testingPair, boolean nullable) {
        final PreSet first = testingPair.first();

        if (nullable) {
            checkAdd(testingPair, Arrays.asList(new TestClass("a"), null, new TestClassDerived("b"), null, new TestClassDerived("a"), new TestClass("b")));
        } else {
            checkAdd(testingPair, Arrays.asList(new TestClass("a"), new TestClassDerived("b"), new TestClassDerived("a"), new TestClass("b")));
            Asserts.assertThrow(() -> first.add(null), NullPointerException.class, first + ".add(null) is expected to throw NullPointerException");
        }
        checkAdd(testingPair, Arrays.asList(new TestClass("aaa"), new TestClassDerived("bb"), new TestClass("cc")));
        if (nullable) {
            checkContains(testingPair, Arrays.asList(new TestClass("a"), null, new TestClassDerived("b"), new TestClassDerived("a"), new TestClass("b"), new TestClass("c"), new TestClassDerived("cc")));
        } else {
            checkContains(testingPair, Arrays.asList(new TestClass("a"), new TestClassDerived("b"), new TestClassDerived("a"), new TestClass("b"), new TestClass("c"), new TestClassDerived("cc")));
            Asserts.assertThrow(() -> first.contains(null), NullPointerException.class, first + ".contains(null) is expected to throw NullPointerException");
        }
        Asserts.assertThrow(() -> first.contains(new Object()), ClassCastException.class, first + ".contains(new Object()) is expected to throw ClassCastException");
        checkClear(testingPair);
        if (nullable) {
            checkRemove(testingPair, Arrays.asList(null, new TestClass("a"), new TestClassDerived("b"), new TestClass("cc")));
        } else {
            checkRemove(testingPair, Arrays.asList(new TestClass("a"), new TestClassDerived("b"), new TestClass("cc")));
        }
        Asserts.assertThrow(() -> first.remove(123), ClassCastException.class, first + ".remove(123) is expected to throw ClassCastException");

        checkAdd(testingPair, Arrays.asList(new TestClassDerived("a"), new TestClassDerived("b"), new TestClass("c"), new TestClass("d")));
        checkAdd(testingPair, Arrays.asList(new TestClass("aaaaaaa"), new TestClassDerived("bcd"), new TestClass("")));
        {
            final TreeSet<TestClass> containsCheck = new TreeSet<>(testingPair.second().comparator());
            containsCheck.addAll(Arrays.asList(new TestClassDerived("c"), new TestClass("b")));
            final TreeSet<TestClass> toRemove = new TreeSet<>(testingPair.second().comparator());
            toRemove.add(new TestClassDerived("c"));
            toRemove.add(new TestClass("b"));
            if (nullable) {
                containsCheck.add(null);
                toRemove.add(null);
            }
            checkContains(testingPair, containsCheck);
            checkIteratorRemove(testingPair, toRemove);
        }
        checkAdd(testingPair, Arrays.asList(new TestClassDerived("?"), new TestClass("#"), new TestClassDerived("??")));

        {
            final TreeSet<TestClass> toRemove = new TreeSet<>(testingPair.second().comparator());
            toRemove.addAll(List.of(new TestClassDerived("bcd"), new TestClass("###"), new TestClassDerived("!!"), new TestClass("a")));
            if (nullable) {
                toRemove.add(null);
            }
            checkRemove(testingPair, toRemove);
        }
        if (!nullable) {
            Asserts.assertThrow(() -> first.remove(null), NullPointerException.class, first + ".remove(null) is expected to throw NullPointerException");
        }
        Asserts.assertThrow(() -> first.remove('a'), ClassCastException.class, first + ".remove('a') is expected to throw ClassCastException");
    }

    public static void main(String[] args) {
        final TreePreSetTest test = new TreePreSetTest();

        test.main(Pair.of(new TreePreSet(TestClass.SENSITIVE_COMPARATOR), new TreeSet<>(TestClass.SENSITIVE_COMPARATOR)), true);
        test.main(Pair.of(new TreePreSet(TestClass.LENGTH_COMPARATOR), new TreeSet<>(TestClass.LENGTH_COMPARATOR)), false);
    }
}

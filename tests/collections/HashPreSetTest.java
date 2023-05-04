package collections;

import base.pairs.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashPreSetTest extends PreSetTest {
    public static void main(final String[] args) {
        final HashPreSetTest test = new HashPreSetTest();
        final Pair<? extends PreSet, ? extends Set<TestClass>> testingPair = Pair.of(new HashPreSet(), new HashSet<>());
        test.checkAdd(testingPair, Arrays.asList(new TestClass("a"), null, new TestClassDerived("b"), null, new TestClassDerived("a"), new TestClass("b")));
        test.checkAdd(testingPair, Arrays.asList(new TestClass("aaa"), new TestClassDerived("bb"), new TestClass("cc")));
        test.checkContains(testingPair, Arrays.asList(new TestClass("a"), null, new Object(), "a", new TestClassDerived("b"), new TestClassDerived("a"), new TestClass("b"), new TestClass("c"), new TestClassDerived("cc")));
        test.checkClear(testingPair);
        test.checkRemove(testingPair, Arrays.asList(null, new TestClassBase("a"), new TestClassBase("b"), new Object(), "b", new TestClass("cc")));
        test.checkAdd(testingPair, Arrays.asList(new TestClassDerived("a"), new TestClassDerived("b"), new TestClass("c"), new TestClass("d")));
        test.checkAdd(testingPair, Arrays.asList(new TestClass("aaaaaaa"), new TestClassDerived("bcd"), new TestClass("")));
        test.checkContains(testingPair, Arrays.asList(null, new TestClassBase("a"), new TestClassBase("b"), new TestClass("a"), new TestClass("b"), new Object(), "a", new TestClassBase("")));
        test.checkIteratorRemove(testingPair, new HashSet<>(Arrays.asList(null, new TestClassBase("c"), new TestClass("b"), new Object(), "a", "b")));
        test.checkAdd(testingPair, Arrays.asList(new TestClassDerived("?"), new TestClass("#"), new TestClassDerived("!")));
        test.checkRemove(testingPair, Arrays.asList(null, new TestClassBase("c"), new TestClass("?"), new TestClassBase("!"), new TestClassBase("a"), new Object()));
    }
}

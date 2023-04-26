package collections;

import base.Asserts;
import base.IndentingWriter;
import base.pairs.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.*;

public class SimplePreListTest {
    protected static final IndentingWriter writer = new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    protected static void checkElements(final SimplePreList list, final List<? super TestClass> expected) throws IOException {
        writer.write("Testing equality of " + list + " and " + expected + "\n");
        Asserts.assertEquals(list.size(), expected.size(), "Size is expected to be " + expected.size());
        Asserts.assertEquals(list.isEmpty(), expected.isEmpty(), "IsEmpty is expected to be " + expected.isEmpty());
        for (int i = 0; i < expected.size(); i++) {
            Asserts.assertIdentical(list.get(i), expected.get(i), "Get(" + i + ") is expected to be IDENTICAL to " + expected.get(i));
        }
        writer.write("Testing iterator of " + list + "\n");
        final Iterator<TestClass> it = list.iterator();
        for (int i = 0; i < expected.size(); i++) {
            Asserts.assertTrue(it.hasNext(), i + "'th hasNext() is expected to be true");
            Asserts.assertIdentical(it.next(), expected.get(i), i + "'th next() call is expected to return element IDENTICAL to " + expected.get(i));
        }
        Asserts.assertFalse(it.hasNext(), expected.size() + "'th hasNext() is expected to be false");
    }

    protected static void checkContains(final SimplePreList list) throws IOException {
        writer.write("Testing contains & indexOf of " + list + "\n");

        final Map<TestClass, Integer> elements = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            elements.putIfAbsent(list.get(i), i);
        }
        for (final Map.Entry<TestClass, Integer> entry : elements.entrySet()) {
            Asserts.assertEquals(list.indexOf(entry.getKey()), entry.getValue(), "indexOf(" + entry.getKey() + ") is expected to be " + entry.getValue());
            Asserts.assertTrue(list.contains(entry.getKey()), "contains(" + entry.getKey() + ") is expected to be true");
            if (entry.getKey() != null) {
                final Object elementCopy = entry.getKey().copyBase();
                Asserts.assertEquals(list.indexOf(elementCopy), entry.getValue(), "indexOf(" + elementCopy + ") is expected to be " + entry.getValue());
                Asserts.assertTrue(list.contains(elementCopy), "contains(" + elementCopy + ") is expected to be true");
            }
        }
    }

    protected static void checkNotContains(final SimplePreList list, final Collection<? super TestClass> notExisting) {
        for (final Object element : notExisting) {
            Asserts.assertEquals(list.indexOf(element), -1, "indexOf(" + element + ") is expected to be -1");
            Asserts.assertFalse(list.contains(element), "contains(" + element + ") is expected to be false");
            if (element instanceof TestClass testClass) {
                final Object elementCopy = testClass.copyBase();

                Asserts.assertEquals(list.indexOf(elementCopy), -1, "indexOf(" + elementCopy + ") is expected to be -1");
                Asserts.assertFalse(list.contains(elementCopy), "contains(" + elementCopy + ") is expected to be false");
            }
        }
    }

    protected static void checkNonModifying(final SimplePreList list, final List<? super TestClass> initial, final Collection<? super TestClass> notExisting) throws IOException {
        writer.write("Testing non-modifying operations of " + list + "\n");
        writer.scope(() -> {
            checkElements(list, initial);
            checkContains(list);
            checkNotContains(list, notExisting);
        });
    }

    protected static void doSet(final SimplePreList list, final List<TestClass> initial,
                                           final Collection<Pair<Integer, TestClass>> modifications) throws IOException {
        writer.write("Testing set\n");
        writer.scope(() -> {
            for (final Pair<Integer, TestClass> mod : modifications) {
                Asserts.assertIdentical(initial.get(mod.first()), list.set(mod.first(), mod.second()),
                        "set(" + mod.first() + ", " + mod.second() + ") to return IDENTICAL to " + initial.get(mod.first()));
                initial.set(mod.first(), mod.second());
                writer.write("set(" + mod.first() + ", " + mod.second() + ")\n");
            }
        });
    }

    protected static void doAddToEnd(final SimplePreList list, final List<TestClass> initial, final Collection<TestClass> addendum) throws IOException {
        initial.addAll(addendum);
        writer.write("Adding " + addendum + " to " + list + "\n");
        for (final TestClass elem : addendum) {
            Asserts.assertTrue(list.add(elem), "Add is expected to return true");
        }
    }

    protected static void doAddIndexed(final SimplePreList list, final List<TestClass> initial, final Collection<Pair<Integer, TestClass>> insertions) throws IOException {
        writer.write("Testing indexed adding to " + list + "\n");
        for (final Pair<Integer, TestClass> mod : insertions) {
            initial.add(mod.first(), mod.second());
            list.add(mod.first(), mod.second());
            writer.write("add(" + mod.first() + ", " + mod.second() + ")\n");
        }
    }

    protected static void gc() {
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
    }

    protected static void doClear(final SimplePreList list) throws IOException {
        writer.write("Testing clear of " + list + "\n");
        writer.scope(() -> {
            final ArrayList<WeakReference<TestClassBase>> weakReferences = new ArrayList<>();
            for (final TestClass testClass : list) {
                if (testClass != null) {
                    weakReferences.add(testClass.selfReference);
                }
            }
            list.clear();
            gc();
            for (final WeakReference<TestClassBase> ref : weakReferences) {
                final TestClassBase testClass = ref.get();
                Asserts.assertNull(testClass, "TestClass " + testClass + " is expected not to exist. If you're ABSOLUTELY SURE it's wrong, comment that line");
            }
        });
    }

    protected static void checkRemoveIndexed(final SimplePreList list, final List<TestClass> initial, final Collection<Integer> indices) throws IOException {
        writer.write("Removing indices " + indices + " out of " + list + "\n");
        writer.scope(() -> {
            final ArrayList<WeakReference<TestClassBase>> weakReferences = new ArrayList<>();
            for (final Integer index : indices) {
                if (list.get(index) != null) {
                    weakReferences.add(list.get(index).selfReference);
                }
                Asserts.assertIdentical(list.remove(index.intValue()), initial.get(index),
                        "Expected remove(" + index + ") to return IDENTICAL " + initial.get(index));
                initial.remove(index.intValue());
            }
            gc();
            for (final WeakReference<TestClassBase> ref : weakReferences) {
                final TestClassBase testClass = ref.get();
                Asserts.assertNull(testClass, "TestClass " + testClass + " is expected not to exist. If you're ABSOLUTELY SURE it's wrong, comment that line");
            }
            checkElements(list, initial);
        });
    }

    protected static void checkRemove(final SimplePreList list, final List<TestClass> initial, final Collection<?> objects) throws IOException {
        writer.write("Removing " + objects + " out of " + list + "\n");
        writer.scope(() -> {
            final ArrayList<WeakReference<TestClassBase>> weakReferences = new ArrayList<>();
            for (final Object object : objects) {
                int index = list.indexOf(object);
                if (index != -1 && list.get(index) != null) {
                    weakReferences.add(list.get(index).selfReference);
                }
                if (initial.remove(object)) {
                    Asserts.assertTrue(list.remove(object), "Expected removing " + object + " return true");
                } else {
                    Asserts.assertFalse(list.remove(object), "Expected removing " + object + " return false");
                }
            }
            gc();
            for (final WeakReference<TestClassBase> ref : weakReferences) {
                final TestClassBase testClass = ref.get();
                Asserts.assertNull(testClass, "TestClass " + testClass + " is expected not to exist. If you're ABSOLUTELY SURE it's wrong, comment that line");
            }
            checkElements(list, initial);
        });
    }

    protected static void doIteratorRemove(final SimplePreList list, final List<TestClass> current, final SortedSet<Integer> toRemove) throws IOException {
        writer.write("Removing elements with the following indices: " + toRemove + " using iterator.remove\n");
        int i = 0;
        final ArrayList<WeakReference<TestClassBase>> weakReferences = new ArrayList<>();
        for (final Iterator<TestClass> actual = list.iterator(), expected = current.iterator(); actual.hasNext();) {
            final TestClass elem = actual.next();
            expected.next();
            if (toRemove.contains(i++)) {
                if (elem != null) {
                    weakReferences.add(elem.selfReference);
                }
                actual.remove();
                expected.remove();
            }
        }
        gc();
        for (final WeakReference<TestClassBase> ref : weakReferences) {
            final TestClassBase testClass = ref.get();
            Asserts.assertNull(testClass, "TestClass " + testClass + " is expected not to exist. If you're ABSOLUTELY SURE it's wrong, comment that line");
        }
    }

    protected static void checkViewingArrayAsPreList() throws IOException {
        writer.write("Checking ArrayAsPreList being view\n");
        writer.scope(() -> {
            final TestClass[] array = new TestClass[]{new TestClass("a"), new TestClass("b"), new TestClass("c")};
            final ComplexPreList preList = new ArrayAsPreList(array);
            final TestClass e1 = new TestClass("new");
            preList.set(1, e1);
            Asserts.assertIdentical(e1, array[1], "Source array is expected to change after ArrayAsPreList changed.");

            final TestClass e2 = new TestClass("meow");
            array[2] = e2;
            Asserts.assertIdentical(e2, preList.get(2), "ArrayAsPreList is expected to change after source array changed.");
        });
    }

    public static void main(String[] args) throws IOException {
        writer.write("Testing ArrayPreList\n");
        writer.scope(() -> {
            SimplePreList preList = new ArrayPreList();
            checkNonModifying(preList, List.of(), Arrays.asList(123, new Object(), "abc", new TestClass("test"), new TestClassBase("base"), null));

            ArrayList<TestClass> current =
                    new ArrayList<>(Arrays.asList(null, new TestClass("a"), new TestClass("b"), null, new TestClass("c"), null));
            doAddToEnd(preList, new ArrayList<>(), current);
            checkNonModifying(preList, current, List.of(new TestClass("xx")));
            doSet(preList, current, List.of(Pair.of(1, null), Pair.of(4, new TestClass("c?")), Pair.of(1, new TestClass("a?"))));
            doSet(preList, current, List.of(Pair.of(0, new TestClass("-a")), Pair.of(3, new TestClass("bc")), Pair.of(5, new TestClass("cd"))));
            checkNonModifying(preList, current, Collections.singletonList(null));
            doAddIndexed(
                    preList, current,
                    List.of(Pair.of(0, new TestClass("-a")), Pair.of(0, null), Pair.of(0, new TestClass("-b")), Pair.of(7, null), Pair.of(current.size() + 3, null), Pair.of(current.size() + 4, new TestClass("d")))
            );
            checkNonModifying(preList, current, List.of());
            current = null;
            System.out.print(Objects.toString(current).repeat(0));
            doClear(preList);
            checkElements(preList, List.of());

            current = new ArrayList<>();
            doAddIndexed(
                    preList, current,
                    List.of(
                            Pair.of(0, new TestClass("a")),
                            Pair.of(1, new TestClass("a")),
                            Pair.of(2, null),
                            Pair.of(3, new TestClass("b")),
                            Pair.of(0, new TestClass("a")),
                            Pair.of(4, null),
                            Pair.of(5, new TestClass("a"))
                    )
            );
            checkNonModifying(preList, current, List.of());
            checkRemove(preList, current, Arrays.asList(new TestClass("a"), new TestClass("xx"), new TestClass("a"), null, new TestClass("b"), new TestClass("b")));
            checkRemoveIndexed(preList, current, List.of(0, 1, 0));

            current = new ArrayList<>();
            doAddToEnd(
                    preList, current,
                    Arrays.asList(
                            new TestClass("a"),
                            new TestClass("b"),
                            new TestClass("a"),
                            null,
                            null,
                            new TestClass("c"),
                            new TestClass("b"),
                            new TestClass("a")
                    )
            );
            checkElements(preList, current);
            doIteratorRemove(preList, current, new TreeSet<>(List.of(1, 2, 4, 5, 6, 7)));
            checkNonModifying(preList, current, List.of(new TestClass("c"), new TestClass("b")));

            preList.clear();
            writer.write("Testing IndexOutOfBoundsException\n");
            Asserts.assertThrow(() -> preList.get(0), IndexOutOfBoundsException.class, preList + ".get(0) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(-1), IndexOutOfBoundsException.class, preList + ".get(-1) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(0, null), IndexOutOfBoundsException.class, preList + ".set(0, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(-1, new TestClass("example")), IndexOutOfBoundsException.class, preList + ".get(-1, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.add(1, null), IndexOutOfBoundsException.class, preList + ".add(1, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.add(-1, new TestClass("example")), IndexOutOfBoundsException.class, preList + ".add(-1, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.remove(0), IndexOutOfBoundsException.class, preList + ".remove(0) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.remove(-1), IndexOutOfBoundsException.class, preList + ".remove(-1) is expected to throw IndexOutOfBoundsException");
            preList.add(null);
            preList.add(new TestClass("abc"));
            preList.add(null);
            Asserts.assertThrow(() -> preList.get(3), IndexOutOfBoundsException.class, preList + ".get(3) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(-1), IndexOutOfBoundsException.class, preList + ".get(-1) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(-17), IndexOutOfBoundsException.class, preList + ".get(-17) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(4), IndexOutOfBoundsException.class, preList + ".get(4) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(123), IndexOutOfBoundsException.class, preList + ".get(123) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(3, null), IndexOutOfBoundsException.class, preList + ".set(3, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(-1, null), IndexOutOfBoundsException.class, preList + ".set(-1, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(-17, new TestClass("")), IndexOutOfBoundsException.class, preList + ".set(-17, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(4, new TestClass("_")), IndexOutOfBoundsException.class, preList + ".set(4, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(123, null), IndexOutOfBoundsException.class, preList + ".set(123, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.add(-1, null), IndexOutOfBoundsException.class, preList + ".add(-1, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.add(-17, new TestClass("")), IndexOutOfBoundsException.class, preList + ".add(-17, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.add(4, new TestClass("_")), IndexOutOfBoundsException.class, preList + ".add(4, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.add(123, null), IndexOutOfBoundsException.class, preList + ".add(123, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.remove(3), IndexOutOfBoundsException.class, preList + ".remove(3) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.remove(-1), IndexOutOfBoundsException.class, preList + ".remove(-1) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.remove(-17), IndexOutOfBoundsException.class, preList + ".remove(-17) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.remove(4), IndexOutOfBoundsException.class, preList + ".remove(4) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.remove(123), IndexOutOfBoundsException.class, preList + ".remove(123) is expected to throw IndexOutOfBoundsException");

            writer.write("Testing IllegalStateException & NoSuchElementException\n");
            final Iterator<TestClass> iterator = preList.iterator();
            Asserts.assertThrow(iterator::remove, IllegalStateException.class, preList + ".iterator().remove() remove is expected to throw IllegalStateException");
            iterator.next();
            iterator.remove();
            Asserts.assertThrow(iterator::remove, IllegalStateException.class, preList + "iterator twice removal is expected to throw IllegalStateException");
            iterator.next();
            iterator.next();
            Asserts.assertThrow(iterator::next, NoSuchElementException.class, preList + "'s past-to-end iterator's next is expected to throw NoSuchElementException");
        });

        writer.write("Testing ArrayAsPreList\n");
        writer.scope(() -> {
            final TestClass[] base = new TestClass[]{null, new TestClass("1"), new TestClass("2"), null, null, new TestClass("5")};
            SimplePreList preList = new ArrayAsPreList(base);
            checkNonModifying(preList, Arrays.asList(base), Arrays.asList(123, new Object(), "abc", new TestClass("test"), new TestClassBase("base")));

            doSet(preList, Arrays.asList(base), List.of(Pair.of(5, new TestClass("5")), Pair.of(0, new TestClass("0")), Pair.of(4, new TestClass("4")), Pair.of(3, new TestClass("3"))));
            checkNonModifying(preList, Arrays.asList(base), Collections.singletonList(null));

            writer.write("Testing IndexOutOfBoundsException\n");
            Asserts.assertThrow(() -> preList.get(6), IndexOutOfBoundsException.class, preList + ".get(6) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(-1), IndexOutOfBoundsException.class, preList + ".get(-1) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(-17), IndexOutOfBoundsException.class, preList + ".get(-17) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(7), IndexOutOfBoundsException.class, preList + ".get(7) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.get(123), IndexOutOfBoundsException.class, preList + ".get(123) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(6, null), IndexOutOfBoundsException.class, preList + ".set(6, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(-1, null), IndexOutOfBoundsException.class, preList + ".set(-1, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(-17, new TestClass("")), IndexOutOfBoundsException.class, preList + ".set(-17, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(7, new TestClass("_")), IndexOutOfBoundsException.class, preList + ".set(7, ...) is expected to throw IndexOutOfBoundsException");
            Asserts.assertThrow(() -> preList.set(123, null), IndexOutOfBoundsException.class, preList + ".set(123, ...) is expected to throw IndexOutOfBoundsException");

            writer.write("Testing UnsupportedOperationException, IllegalStateException & NoSuchElementException\n");
            Asserts.assertThrow(() -> preList.add(null), UnsupportedOperationException.class, preList + ".add(...) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.add(0, null), UnsupportedOperationException.class, preList + ".add(0, ...) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.add(3, null), UnsupportedOperationException.class, preList + ".add(3, ...) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.add(6, null), UnsupportedOperationException.class, preList + ".add(6, ...) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.add(-1, null), UnsupportedOperationException.class, preList + ".add(-1, ...) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.add(7, null), UnsupportedOperationException.class, preList + ".add(7, ...) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(preList::clear, UnsupportedOperationException.class, preList + ".clear() is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.remove(0), UnsupportedOperationException.class, preList + ".remove(0) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.remove(5), UnsupportedOperationException.class, preList + ".remove(5) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.remove(-1), UnsupportedOperationException.class, preList + ".remove(-1) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.remove(100), UnsupportedOperationException.class, preList + ".remove(100) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.remove(new Object()), UnsupportedOperationException.class, preList + ".remove(new Object()) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.remove(null), UnsupportedOperationException.class, preList + ".remove(null) is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> preList.remove(new TestClass("1")), UnsupportedOperationException.class, preList + ".remove(new TestClass(\"1\")) is expected to throw UnsupportedOperationException");

            final Iterator<TestClass> iterator = preList.iterator();
            Asserts.assertThrow(
                    iterator::remove,
                    List.of(UnsupportedOperationException.class, IllegalStateException.class),
                    preList + " iterators' remove is expected to throw UnsupportedOperationException or IllegalStateException"
            );
            iterator.next();
            Asserts.assertThrow(iterator::remove, UnsupportedOperationException.class, preList + " iterators' remove is expected to throw UnsupportedOperationException");
            iterator.next();
            iterator.next();
            Asserts.assertThrow(iterator::remove, UnsupportedOperationException.class, preList + " iterators' remove is expected to throw UnsupportedOperationException");
            iterator.next();
            iterator.next();
            iterator.next();
            Asserts.assertThrow(iterator::next, NoSuchElementException.class, preList + " iterators' remove is expected to throw NoSuchElementException");

            checkViewingArrayAsPreList();
        });
    }
}

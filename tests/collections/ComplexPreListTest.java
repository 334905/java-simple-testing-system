package collections;

import base.Asserts;
import base.pairs.Pair;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;

public class ComplexPreListTest extends SimplePreListTest {
    protected static void checkContains(final ComplexPreList list) throws IOException {
        SimplePreListTest.checkContains(list);

        writer.write("Testing lastIndexOf of " + list + "\n");
        final Map<TestClass, Integer> elements = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            elements.put(list.get(i), i);
        }
        for (final Map.Entry<TestClass, Integer> entry : elements.entrySet()) {
            Asserts.assertEquals(list.lastIndexOf(entry.getKey()), entry.getValue(), "lastIndexOf(" + entry.getKey() + ") is expected to be " + entry.getValue());
            if (entry.getKey() != null) {
                final Object elementCopy = entry.getKey().copyBase();
                Asserts.assertEquals(list.lastIndexOf(elementCopy), entry.getValue(), "lastIndexOf(" + elementCopy + ") is expected to be " + entry.getValue());
            }
        }
    }

    protected static void doListIteratorSet(final ComplexPreList list, final List<TestClass> initial,
                                            final Collection<Pair<Integer, TestClass>> modifications) throws IOException {
        writer.write("Testing listIterator's set\n");
        writer.scope(() -> {
            final ListIterator<TestClass> iter = list.listIterator();
            int index = 0;
            for (final Pair<Integer, TestClass> mod : modifications) {
                if (index <= mod.first()) {
                    while (index <= mod.first()) {
                        index++;
                        iter.next();
                    }
                } else {
                    while (index > mod.first()) {
                        index--;
                        iter.previous();
                    }
                }
                iter.set(mod.second());

                initial.set(mod.first(), mod.second());
                writer.write("set(" + mod.first() + ", " + mod.second() + ")\n");
            }
        });
    }

    protected static void doListIteratorAdd(final ComplexPreList list, final List<TestClass> initial, final Collection<Pair<Integer, TestClass>> insertions) throws IOException {
        writer.write("Testing adding to " + list + " using listIterator\n");
        int index = 0;
        final ListIterator<TestClass> iter = list.listIterator();
        for (final Pair<Integer, TestClass> mod : insertions) {
            if (index < mod.first()) {
                while (index < mod.first()) {
                    index++;
                    iter.next();
                }
            } else {
                while (index > mod.first()) {
                    index--;
                    iter.previous();
                }
            }
            initial.add(mod.first(), mod.second());
            iter.add(mod.second());
            index++;
            writer.write("add(" + mod.first() + ", " + mod.second() + ")\n");
        }
    }

    protected static void checkListIteratorRemove(final ComplexPreList list, final List<TestClass> initial, final Collection<Integer> indices) throws IOException {
        writer.write("Removing indices " + indices + " out of " + list + " using listIterator\n");
        writer.scope(() -> {
            int currIndex = 0;
            final ListIterator<TestClass> iter = list.listIterator();

            final ArrayList<WeakReference<TestClassBase>> weakReferences = new ArrayList<>();
            for (final Integer index : indices) {
                if (currIndex <= index) {
                    while (currIndex <= index) {
                        currIndex++;
                        iter.next();
                    }
                    currIndex--;
                } else {
                    while (currIndex > index) {
                        currIndex--;
                        iter.previous();
                    }
                }
                if (list.get(index) != null) {
                    weakReferences.add(list.get(index).selfReference);
                }
                iter.remove();
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

    private static <E> void testListIterator(final ListIterator<? super E> it, final int start, final List<? super E> expected) throws IOException {
        writer.write("    Forward pass\n");
        if (start == 0) {
            Asserts.assertFalse(it.hasPrevious(), "0'th hasPrevious() is expected to be false");
        } else {
            Asserts.assertTrue(it.hasPrevious(), start + "'th hasPrevious() is expected to be false");
        }
        for (int i = start; i < expected.size(); i++) {
            Asserts.assertIdentical(it.nextIndex(), i, i + "'th nextIndex() is expected to be " + i);
            Asserts.assertIdentical(it.previousIndex(), i - 1, i + "'th previousIndex() is expected to be " + (i - 1));
            Asserts.assertTrue(it.hasNext(), i + "'th hasNext() is expected to be true");
            Asserts.assertIdentical(it.next(), expected.get(i), i + "'th next() call is expected to return element IDENTICAL to " + expected.get(i));

            Asserts.assertTrue(it.hasPrevious(), i + 1 + "'th hasPrevious() is expected to be true");
        }
        Asserts.assertFalse(it.hasNext(), expected.size() + "'th hasNext() is expected to be false");

        writer.write("    Backward pass\n");
        for (int i = expected.size(); i > 0; i--) {
            Asserts.assertIdentical(it.nextIndex(), i, i + "'th nextIndex() is expected to be " + i);
            Asserts.assertIdentical(it.previousIndex(), i - 1, i + "'th previousIndex() is expected to be " + (i - 1));
            Asserts.assertTrue(it.hasPrevious(), i + "'th hasPrevious() is expected to be true");
            Asserts.assertIdentical(it.previous(), expected.get(i - 1), i + "'th previous() call is expected to return element IDENTICAL to " + expected.get(i - 1));

            Asserts.assertTrue(it.hasNext(), i - 1 + "'th hasNext() is expected to be true");
        }
        Asserts.assertIdentical(it.nextIndex(), 0, "0'th nextIndex() is expected to be 0");
        Asserts.assertIdentical(it.previousIndex(), -1, "0'th previousIndex() is expected to be -1");
        Asserts.assertFalse(it.hasPrevious(), 0 + "'th hasPrevious() is expected to be false");
    }

    protected static void checkElements(final ComplexPreList list, final List<? super TestClass> expected) throws IOException {
        SimplePreListTest.checkElements(list, expected);

        writer.write("Testing ListIterator-s of " + list + "\n");
        writer.scope(() -> {
            writer.write("Testing listIterator()\n");
            writer.scope(() -> testListIterator(list.listIterator(), 0, expected));

            for (int i = 0; i < list.size(); i++) {
                final int ii = i;
                writer.write("Testing listIterator(" + i + ")\n");
                writer.scope(() -> testListIterator(list.listIterator(ii), ii, expected));
            }
        });
    }

    protected static void checkNotContains(final ComplexPreList list, final Collection<? super TestClass> notExisting) {
        SimplePreListTest.checkNotContains(list, notExisting);

        for (final Object element : notExisting) {
            Asserts.assertEquals(list.lastIndexOf(element), -1, "lastIndexOf(" + element + ") is expected to be -1");
            if (element instanceof TestClass testClass) {
                final Object elementCopy = testClass.copyBase();
                Asserts.assertEquals(list.lastIndexOf(elementCopy), -1, "lastIndexOf(" + elementCopy + ") is expected to be -1");
            }
        }
    }

    protected static void checkNonModifying(final ComplexPreList list, final List<? super TestClass> initial, final Collection<? super TestClass> notExisting) throws IOException {
        writer.write("Testing non-modifying operations of " + list + "\n");
        writer.scope(() -> {
            checkElements(list, initial);
            checkContains(list);
            checkNotContains(list, notExisting);
        });
    }

    public static void main(String[] args) throws IOException {
        writer.write("Testing ArrayPreList\n");
        writer.scope(() -> {
            final ComplexPreList preList = new ArrayPreList();
            checkNonModifying(preList, List.of(), Arrays.asList(123, new Object(), "abc", new TestClass("test"), new TestClassBase("base"), null));

            ArrayList<TestClass> current =
                    new ArrayList<>(Arrays.asList(null, new TestClass("a"), new TestClass("b"), null, new TestClass("c"), null));
            doAddToEnd(preList, new ArrayList<>(), current);
            checkNonModifying(preList, current, List.of(new TestClass("xx")));
            doSet(preList, current, List.of(Pair.of(1, null), Pair.of(4, new TestClass("c?")), Pair.of(1, new TestClass("a?"))));
            doSet(preList, current, List.of(Pair.of(0, new TestClass("-a")), Pair.of(3, new TestClass("bc")), Pair.of(5, new TestClass("cd"))));
            checkNonModifying(preList, current, Collections.singletonList(null));
            doListIteratorSet(preList, current, List.of(Pair.of(0, new TestClass("?00?")), Pair.of(2, new TestClass("?22?")), Pair.of(2, new TestClass("22")), Pair.of(4, new TestClass("44")), Pair.of(1, new TestClass("11")), Pair.of(0, new TestClass("00"))));
            checkNonModifying(preList, current, List.of());
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
            checkNonModifying(preList, current, List.of());
            checkRemove(preList, current, Arrays.asList(new TestClass("a"), new TestClass("xx"), new TestClass("a"), null, new TestClass("b"), new TestClass("b")));
            checkRemoveIndexed(preList, current, List.of(0, 1, 0));

            current = new ArrayList<>();
            doListIteratorAdd(
                    preList, current,
                    List.of(
                            Pair.of(0, new TestClass("a")),
                            Pair.of(1, new TestClass("b")),
                            Pair.of(2, null),
                            Pair.of(3, new TestClass("c")),
                            Pair.of(0, new TestClass("d")),
                            Pair.of(4, null),
                            Pair.of(5, new TestClass("e"))
                    )
            );
            checkNonModifying(preList, current, List.of());
            checkListIteratorRemove(preList, current, List.of(4, 5, 1, 2, 0, 1, 0));

            preList.clear();
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

            Asserts.assertThrow(() -> preList.listIterator().previous(), NoSuchElementException.class, preList + "'s pre-to-begin listIterator's previous is expected to throw NoSuchElementException");
            final ListIterator<TestClass> listIterator = preList.listIterator(1);
            Asserts.assertThrow(() -> listIterator.set(null), IllegalStateException.class, preList + " listIterator's set before next() is expected to throw IllegalStateException");
            Asserts.assertThrow(listIterator::remove, IllegalStateException.class, preList + ".listIterator(...).remove() remove is expected to throw IllegalStateException");
            listIterator.next();
            listIterator.remove();
            Asserts.assertThrow(() -> listIterator.set(null), IllegalStateException.class, preList + " listIterator's set after remove() is expected to throw IllegalStateException");
            Asserts.assertThrow(listIterator::remove, IllegalStateException.class, preList + "listIterator twice removal is expected to throw IllegalStateException");
            Asserts.assertThrow(listIterator::next, NoSuchElementException.class, preList + "'s past-to-end listIterator's next is expected to throw NoSuchElementException");
        });

        writer.write("Testing ArrayAsPreList\n");
        writer.scope(() -> {
            final TestClass[] base = new TestClass[]{null, new TestClass("1"), new TestClass("2"), null, null, new TestClass("5")};
            ComplexPreList preList = new ArrayAsPreList(base);
            checkNonModifying(preList, Arrays.asList(base), Arrays.asList(123, new Object(), "abc", new TestClass("test"), new TestClassBase("base")));

            doSet(preList, Arrays.asList(base), List.of(Pair.of(5, new TestClass("5")), Pair.of(0, new TestClass("0")), Pair.of(4, new TestClass("4")), Pair.of(3, new TestClass("3"))));
            checkNonModifying(preList, Arrays.asList(base), Collections.singletonList(null));

            doListIteratorSet(preList, Arrays.asList(base), List.of(Pair.of(0, new TestClass("?00?")), Pair.of(2, new TestClass("?22?")), Pair.of(2, new TestClass("22")), Pair.of(5, new TestClass("55")), Pair.of(1, new TestClass("11")), Pair.of(0, new TestClass("00"))));
            checkNonModifying(preList, Arrays.asList(base), List.of());

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
                    preList + " iterator's remove before next() is expected to throw UnsupportedOperationException or IllegalStateException"
            );
            iterator.next();
            Asserts.assertThrow(iterator::remove, UnsupportedOperationException.class, preList + " iterator's remove is expected to throw UnsupportedOperationException");
            iterator.next();
            iterator.next();
            Asserts.assertThrow(iterator::remove, UnsupportedOperationException.class, preList + " iterator's remove is expected to throw UnsupportedOperationException");
            iterator.next();
            iterator.next();
            iterator.next();
            Asserts.assertThrow(iterator::next, NoSuchElementException.class, preList + " iterator's remove from the end is expected to throw NoSuchElementException");

            Asserts.assertThrow(() -> preList.listIterator().previous(), NoSuchElementException.class, preList + "'s pre-to-begin listIterator's previous is expected to throw NoSuchElementException");
            final ListIterator<TestClass> listIterator = preList.listIterator(4);
            Asserts.assertThrow(
                    listIterator::remove,
                    List.of(UnsupportedOperationException.class, IllegalStateException.class),
                    preList + " listIterator' remove is expected to throw UnsupportedOperationException or IllegalStateException"
            );
            Asserts.assertThrow(() -> listIterator.set(null), IllegalStateException.class, preList + " listIterator's set before next() is expected to throw IllegalStateException");
            listIterator.next();
            Asserts.assertThrow(listIterator::remove, UnsupportedOperationException.class, preList + " listIterator's remove is expected to throw UnsupportedOperationException");
            Asserts.assertThrow(() -> listIterator.add(null), UnsupportedOperationException.class, preList + " listIterator's add is expected to throw UnsupportedOperationException");
            listIterator.next();
            Asserts.assertThrow(listIterator::next, NoSuchElementException.class, preList + " listIterator's remove rom the end is expected to throw NoSuchElementException");

            checkViewingArrayAsPreList();
        });
    }
}

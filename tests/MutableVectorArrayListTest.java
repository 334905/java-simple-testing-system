import base.ExtendedRandom;
import base.IndentingWriter;
import base.expected.Expected;
import base.function.ThrowingConsumer;
import mutable_vector_array_list.MutableVector;
import mutable_vector_array_list.MutableVectorArrayListTester;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class MutableVectorArrayListTest {
    private static void expectTrue(final boolean cond, final String message) {
        if (!cond)
            throw new AssertionError(message);
    }

    private static void expectFalse(final boolean cond, final String message) {
        expectTrue(!cond, message);
    }

    private static void expectIdentical(final Object first, final Object second, final String message) {
        expectTrue(first == second, message);
    }

    private static void expectEqual(final Object first, final Object second, final String message) {
        expectTrue(Objects.equals(first, second), message);
    }

    private static <T extends Comparable<? super T>> void expectLess(final T value, final T upper, final String message) {
        expectTrue(value.compareTo(upper) < 0, message);
    }

    private static <T extends Comparable<? super T>> void expectGreater(final T value, final T lower, final String message) {
        expectTrue(value.compareTo(lower) > 0, message);
    }

    private static <T extends Comparable<? super T>> void expectLessOrEqual(final T value, final T max, final String message) {
        expectTrue(value.compareTo(max) <= 0, message);
    }

    private static <T extends Comparable<? super T>> void expectGreaterOrEqual(final T value, final T min, final String message) {
        expectTrue(value.compareTo(min) >= 0, message);
    }

    private static <T extends Comparable<? super T>> void expectInRange(final T value, final T min, final T upper, final String message) {
        expectLess(value, upper, message);
        expectGreaterOrEqual(value, min, message);
    }

    private static void expectEqual(final List<MutableVector> correctList, final Object list, final MutableVectorArrayListTester tester) throws IllegalAccessException {
        expectEqual(correctList.size(), tester.size(list), "Size is supposed to be " + correctList.size());
        for (int i = 0; i < correctList.size(); i++) {
            final Expected<MutableVector, Exception> getResult = tester.expectedGet(list, i);
            expectTrue(getResult.hasValue(), "Index " + i + " is supposed to be valid");
            expectEqual(correctList.get(i), getResult.getValue(),
                    "Element with index " + i + " is supposed to be " + correctList.get(i));
        }
    }

    private static long supposeMutableVectorsCount()
            throws InterruptedException, IOException {
        System.gc();
        Thread.sleep(1000);
        System.gc();
        Thread.sleep(1000);
        System.gc();
        record ClassInfo(long instances, String className) {
        }
        final Pattern jmapRegex = Pattern.compile("^\\s++\\d++[^\\d]++(\\d++)[^\\d]++\\d++\\s++([^\\s]++)");
        final Process jmapProc = Runtime.getRuntime().exec("jmap -histo " + ProcessHandle.current().pid());
        try {
            return jmapProc.inputReader().lines()
                    .map(s -> {
                        final Matcher match = jmapRegex.matcher(s);
                        if (match.matches()) {
                            return new ClassInfo(Long.parseLong(match.group(1)), match.group(2));
                        } else {
                            return null;
                        }
                    })
                    .filter(c -> c != null && c.className.endsWith("mutable_vector_array_list.MutableVector"))
                    .findAny()
                    .map(ClassInfo::instances)
                    .orElse(0L);
        } finally {
            jmapProc.destroy();
        }
    }

    public static void main(String[] args) throws Exception {
        final MutableVectorArrayListTester tester = new MutableVectorArrayListTester();
        final IndentingWriter writer = new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        final ExtendedRandom random = new ExtendedRandom();

        writer.write("Testing correctness...\n");
        writer.scope(() -> {
            writer.write("Testing empty list...\n");
            writer.scope(() -> {
                final Object list = tester.newList();
                expectEqual(tester.size(list), 0, "Size of empty list is supposed to be 0");
                expectEqual(tester.capacity(list), 0, "Capacity of empty list is supposed to be 0");
                expectTrue(tester.isEmpty(list), "Empty list is supposed to be empty");
            });
            writer.write("Testing empty list with capacity...\n");
            writer.scope(() -> {
                final int capacity = 10000;
                writer.write("Capacity is equal to " + capacity + "\n");

                final Expected<?, Exception> expectedList = tester.expectedNewList(capacity);
                expectTrue(expectedList.hasValue(),
                        "new List(" + capacity + ") is supposed not to throw");
                final Object list = expectedList.getValue();
                expectEqual(tester.size(list), 0, "Size of empty list is supposed to be 0");
                expectEqual(tester.capacity(list), capacity, "Capacity of list is supposed to be " + capacity);
                expectTrue(tester.isEmpty(list), "Empty list is supposed to be empty");
            });
            writer.write("Testing adding and valid get&set...\n");
            writer.scope(() -> {
                final int N = 1000;
                final Object list = tester.newList();

                final IntFunction<MutableVector> addFunk = (v) -> new MutableVector(v, v + 0.5);
                writer.write("Adding " + N + " elements...\n");
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.add(list, addFunk.apply(i)),
                            "add is supposed to return true");
                }
                writer.write(N + " elements added...\nChecking statistics...\n");
                expectEqual(tester.size(list), N,
                        "Size of list is supposed to be " + N);
                expectGreaterOrEqual(tester.capacity(list), N,
                        "Capacity of list is supposed to be >= " + N);
                expectFalse(tester.isEmpty(list),
                        "List is not supposed to be empty");

                writer.write("Checking gets...\n");
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.expectedGet(list, i).hasValue(),
                            "Getting element with index " + i + " is not supposed to throw");
                    expectEqual(tester.expectedGet(list, i).getValue(), addFunk.apply(i),
                            "Element with index " + i + " is supposed to be equal to " + addFunk.apply(i));
                }

                final IntFunction<MutableVector> setFunk = (v) -> new MutableVector(-v, v * -2);
                writer.write("Checking sets...\n");
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.expectedSet(list, i, setFunk.apply(i)).hasValue(),
                            "Setting element with index " + i + " is not supposed to throw");
                    expectTrue(tester.expectedGet(list, i).hasValue(),
                            "Getting element with index " + i + " is not supposed to throw");
                    expectEqual(tester.expectedGet(list, i).getValue(), setFunk.apply(i),
                            "Element with index " + i + " is supposed to be equal to " + setFunk.apply(i));
                }
            });
            writer.write("Checking no re-allocations after constructor of capacity called...\n");
            writer.scope(() -> {
                final int N = 1000;
                writer.write("Initial capacity is equal to " + N + "\n");
                final Expected<?, Exception> expectedList = tester.expectedNewList(N);
                expectTrue(expectedList.hasValue(),
                        "new List(" + N + ") is supposed not to throw");
                final Object list = expectedList.getValue();

                final IntFunction<MutableVector> funk = (v) -> new MutableVector(Math.pow(1.25, v), Double.POSITIVE_INFINITY);

                for (int i = 0; i < N; i++) {
                    expectTrue(tester.add(list, funk.apply(i)), "add is supposed to return true");
                    expectEqual(tester.capacity(list), N,
                            "Capacity is supposed to be equal to " + N);
                }
            });
            writer.write("Testing adding then removing from the end...\n");
            writer.scope(() -> {
                final int N = 1000;
                final Object list = tester.newList(0);

                final IntFunction<MutableVector> funk = (v) -> new MutableVector(0.5 * v + 0.25, 0.5 * v);
                writer.write("Adding " + N + " elements...\n");
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.add(list, funk.apply(i)),
                            "add is supposed to return true");
                }

                final int capacity = tester.capacity(list);
                writer.write("Capacity after adding " + N + " elements is " + capacity + "\n");

                writer.write("Removing " + N + " from the end...\n");
                for (int i = 0; i < N; i++) {
                    expectEqual(tester.size(list), N - i,
                            "After deleting " + i + " elements list size is supposed to be " + (N - i));
                    expectFalse(tester.isEmpty(list),
                            "After deleting " + i + " elements list is not supposed to be empty");
                    expectTrue(tester.expectedGet(list, N - i - 1).hasValue(),
                            "Element with index " + (N - i - 1) + " is supposed to exist before deletion");
                    expectEqual(tester.expectedGet(list, N - i - 1).getValue(), funk.apply(N - i - 1),
                            "Element with index " + (N - i - 1) + " is supposed to be equal to " + funk.apply(N - i - 1));
                    final var removeResult = tester.expectedRemove(list, N - i - 1);
                    expectTrue(removeResult.hasValue(),
                            "Removing element with index " + (N - i - 1) + " is not supposed to throw");
                    expectEqual(removeResult.getValue(), funk.apply(N - i - 1),
                            "Removing element with index " + (N - i - 1) + " is supposed to return " + funk.apply(N - i - 1));
                    expectEqual(tester.capacity(list), capacity,
                            "Capacity after removing " + (i + 1) + " elements is supposed to be " + capacity);
                }
                expectEqual(tester.size(list), 0, "After deleting all elements list size is supposed to be 0");
                expectTrue(tester.isEmpty(list), "After deleting all elements list is supposed to be empty");
            });
            writer.write("Testing adding then removing from the begin...\n");
            writer.scope(() -> {
                final int N = 1000;
                final Object list = tester.newList(0);

                final IntFunction<MutableVector> funk = (v) -> new MutableVector(0.0, 1.1);
                writer.write("Adding " + N + " elements...\n");
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.add(list, funk.apply(i)),
                            "add is supposed to return true");
                }

                final int capacity = tester.capacity(list);
                writer.write("Capacity after adding " + N + " elements is " + capacity + "\n");

                writer.write("Removing " + N + " from the begin...\n");
                for (int i = 0; i < N; i++) {
                    expectEqual(tester.size(list), N - i,
                            "After deleting " + i + " elements list size is supposed to be " + (N - i));
                    expectFalse(tester.isEmpty(list),
                            "After deleting " + i + " elements list is not supposed to be empty");
                    expectTrue(tester.expectedGet(list, 0).hasValue(),
                            "Element with index " + 0 + " is supposed to exist before deletion");
                    expectEqual(tester.expectedGet(list, 0).getValue(), funk.apply(i),
                            "Element with index " + 0 + " is supposed to be equal to " + funk.apply(i));
                    final var removeResult = tester.expectedRemove(list, 0);
                    expectTrue(removeResult.hasValue(),
                            "Removing element with index " + 0 + " is not supposed to throw");
                    expectEqual(removeResult.getValue(), funk.apply(i),
                            "Removing element with index " + 0 + " is supposed to return " + funk.apply(i));
                    expectEqual(tester.capacity(list), capacity,
                            "Capacity after removing " + (i + 1) + " elements is supposed to be " + capacity);
                }
                expectEqual(tester.size(list), 0, "After deleting all elements list size is supposed to be 0");
                expectTrue(tester.isEmpty(list), "After deleting all elements list is supposed to be empty");
            });
            writer.write("Testing removing from the random places...\n");
            writer.scope(() -> {
                final int N = 1000;
                final List<MutableVector> correctList = new ArrayList<>(N);
                final Object userList = tester.newList(N);
                final Supplier<MutableVector> randomVector = () -> new MutableVector(random.nextDouble(), random.nextDouble());

                for (int i = 0; i < N; i++) {
                    final MutableVector vector = randomVector.get();
                    correctList.add(vector);
                    tester.add(userList, vector);
                }

                for (int i = 0; i < N; i++) {
                    int index = random.nextInt(correctList.size());
                    final MutableVector correctRemoved = correctList.remove(index);
                    final var removeResult = tester.expectedRemove(userList, index);
                    expectTrue(removeResult.hasValue(),
                            "Removing element with index " + index + " is not supposed to throw");
                    expectEqual(removeResult.getValue(), correctRemoved,
                            "Removing element with index " + index + " is supposed to return " + correctRemoved);
                }
            });
            writer.write("Testing indexOf and contains...\n");
            writer.scope(() -> {
                final int N = 1000;
                final List<MutableVector> correctList = new ArrayList<>(N);
                final Object userList = tester.newList(N);
                final Supplier<MutableVector> randomVector = () -> new MutableVector(random.nextDouble(), random.nextDouble());

                {
                    final MutableVector vector = randomVector.get();
                    expectEqual(tester.indexOf(userList, vector), -1,
                            "Index of " + vector + " in empty list is supposed to be -1");
                    expectFalse(tester.contains(userList, vector),
                            "Empty list is supposed not to contain " + vector);
                    tester.add(userList, vector);
                    correctList.add(vector);
                }

                for (int i = 1; i < N; i++) {
                    final MutableVector newVector = randomVector.get();
                    final MutableVector oldVector = random.nextElementFrom(correctList);

                    expectEqual(tester.indexOf(userList, newVector), correctList.indexOf(newVector),
                            "Index of " + newVector + " is supposed to be equal to " + correctList.indexOf(newVector));
                    expectEqual(tester.indexOf(userList, oldVector), correctList.indexOf(oldVector),
                            "Index of " + oldVector + " is supposed to be equal to " + correctList.indexOf(oldVector));
                    expectEqual(tester.contains(userList, newVector), correctList.contains(newVector),
                            "List is supposed " + (correctList.contains(newVector) ? "" : "not ") + " to contain " + newVector);
                    expectEqual(tester.contains(userList, oldVector), correctList.contains(oldVector),
                            "List is supposed " + (correctList.contains(oldVector) ? "" : "not ") + " to contain " + oldVector);

                    correctList.add(i % 2 == 0 ? newVector : oldVector);
                    tester.add(userList, i % 2 == 0 ? newVector : oldVector);
                }

                for (final MutableVector vector : correctList) {
                    expectEqual(tester.indexOf(userList, vector), correctList.indexOf(vector),
                            "Index of " + vector + " is supposed to be equal to " + correctList.indexOf(vector));
                    expectTrue(tester.contains(userList, vector),
                            "List is supposed to contain " + vector);
                }
            });
            writer.write("Testing remove(MutableVector)...\n");
            writer.scope(() -> {
                final int N = 1000;
                final Object userList = tester.newList(N);
                final List<MutableVector> correctList = new ArrayList<>(N);
                final IntFunction<MutableVector> vectorByIndex = (i) -> new MutableVector(i % 5, ((i + 2) % 5 - 4) * -1);

                for (int i = 0; i < N; i++) {
                    tester.add(userList, vectorByIndex.apply(i));
                    correctList.add(vectorByIndex.apply(i));
                }

                while (correctList.size() != 0) {
                    final MutableVector vector = vectorByIndex.apply(random.nextInt(5));
                    final boolean correctRes = correctList.remove(vector);
                    final boolean userRes = tester.remove(userList, vector);
                    expectEqual(correctList, userList, tester);
                    expectEqual(correctRes, userRes, "Removing " + vector + " is supposed to return " + correctRes);
                }
            });
            writer.write("Testing equals...\n");
            writer.scope(() -> {
                writer.write("Testing nulls...\n");
                writer.scope(() -> {
                    expectFalse(tester.equals(tester.newList(), null),
                            "[] is supposed not to be equal to null");
                    expectFalse(tester.equals(tester.newList(10), null),
                            "[] (with capacity 10) is supposed not to be equal to null");
                    {
                        final Object list = tester.newList();
                        tester.add(list, new MutableVector(1, 2));
                        tester.add(list, new MutableVector(3, 4));
                        tester.add(list, new MutableVector(-7, 0));
                        expectFalse(tester.equals(list, null),
                                "[{1, 2}, {3, 4}, {-7, 0}] is supposed not to be equal to null");
                    }
                });
                writer.write("Testing equals of non-lists...\n");
                writer.scope(() -> {
                    expectFalse(tester.equals(tester.newList(), new Object()),
                            "[] is supposed not to be equal to new Object");
                    expectFalse(tester.equals(tester.newList(10), new ArrayList<MutableVector>(10)),
                            "[] (with capacity 10) is supposed not to be equal to new java.util.ArrayList(10)");
                    {
                        final Object list = tester.newList();
                        tester.add(list, new MutableVector(0.25, -0.5));
                        tester.add(list, new MutableVector(3.2, 0.42));
                        tester.add(list, new MutableVector(-7.1, 0));
                        expectFalse(
                                tester.equals(
                                        list,
                                        List.of(
                                                new MutableVector(0.25, -0.5),
                                                new MutableVector(3.2, 0.42),
                                                new MutableVector(-7.1, 0)
                                        )
                                ),
                                "[{1, 2}, {3, 4}, {-7, 0}] is supposed not to be equal to List.of(<same vectors>)");
                    }
                });
                writer.write("Testing equals of lists...\n");
                writer.scope(() -> {
                    final Object list1 = tester.newList();
                    tester.add(list1, new MutableVector(0.25, -0.5));
                    tester.add(list1, new MutableVector(3.2, 0.42));
                    tester.add(list1, new MutableVector(-7.1, 0));
                    final Object list2 = tester.newList(1000);
                    tester.add(list2, new MutableVector(0.25, -0.5));
                    tester.add(list2, new MutableVector(3.2, 0.42));
                    tester.add(list2, new MutableVector(-7.1, 0));
                    final Object list3 = tester.newList(1000);
                    tester.add(list3, new MutableVector(0.25, -0.5));
                    tester.add(list3, new MutableVector(3.2, 0.42));
                    final Object list4 = tester.newList(1000);
                    tester.add(list4, new MutableVector(-7.1, 0));
                    tester.add(list4, new MutableVector(0.25, -0.5));
                    tester.add(list4, new MutableVector(3.2, 0.42));

                    expectTrue(tester.equals(list4, list4),
                            "List is supposed to be equals to itself");
                    expectTrue(tester.equals(list2, list2),
                            "List is supposed to be equals to itself");
                    expectTrue(tester.equals(list1, list2),
                            "Two identical lists with the different capacity is supposed to be equals");
                    expectFalse(tester.equals(list2, list3),
                            "Two lists with the different size is not supposed to be equals");
                    expectFalse(tester.equals(list2, list4),
                            "Two lists with the different elements order is not supposed to be equals");
                });
            });
            writer.write("Testing ensureCapacity & trimToSize...\n");
            writer.scope(() -> {
                writer.write("Testing capacity changing...\n");
                writer.scope(() -> {
                    final Expected<?, Exception> expectedList = tester.expectedNewList(0);
                    expectTrue(expectedList.hasValue(),
                            "new List(0) is supposed not to throw");
                    final Object list = expectedList.getValue();
                    final IntFunction<MutableVector> vectorFunction =
                            (i) -> new MutableVector((i - 5) * 1.5, -i);
                    tester.add(list, vectorFunction.apply(0));
                    tester.add(list, vectorFunction.apply(1));
                    expectTrue(tester.expectedEnsureCapacity(list, 6).hasValue(),
                            "ensureCapacity(5) is supposed not to throw");
                    expectEqual(List.of(vectorFunction.apply(0), vectorFunction.apply(1)), list, tester);

                    final int currentCapacity = tester.capacity(list);
                    expectGreaterOrEqual(currentCapacity, 6,
                            "Capacity is supposed to be >= than the ensureCapacity argument");

                    for (int i = 2; i < currentCapacity; i++) {
                        tester.add(list, vectorFunction.apply(i));
                        expectEqual(tester.capacity(list), currentCapacity,
                                "Capacity is supposed not to change after adding elements into big enough list");
                    }
                    expectEqual(IntStream.range(0, currentCapacity).mapToObj(vectorFunction).toList(), list, tester);
                    tester.trimToSize(list);
                    expectEqual(tester.capacity(list), currentCapacity,
                            "trimToSize is not supposed to reduce capacity if list's capacity is full");
                    expectEqual(IntStream.range(0, currentCapacity).mapToObj(vectorFunction).toList(), list, tester);

                    tester.expectedRemove(list, 0).getValue();
                    tester.trimToSize(list);
                    expectEqual(tester.capacity(list), currentCapacity - 1,
                            "Capacity is supposed to reduce after element removed and trimmed to size");
                    expectEqual(IntStream.range(1, currentCapacity).mapToObj(vectorFunction).toList(), list, tester);

                    tester.expectedRemove(list, currentCapacity - 2).getValue();
                    tester.expectedRemove(list, currentCapacity - 3).getValue();
                    tester.expectedRemove(list, currentCapacity - 4).getValue();
                    tester.expectedRemove(list, currentCapacity - 5).getValue();
                    tester.trimToSize(list);
                    expectEqual(tester.capacity(list), currentCapacity - 5,
                            "Capacity is supposed to reduce after element removed and trimmed to size");
                    expectEqual(IntStream.range(1, currentCapacity - 4).mapToObj(vectorFunction).toList(), list, tester);

                    tester.clear(list);
                    tester.trimToSize(list);
                    expectEqual(tester.capacity(list), 0,
                            "Capacity is supposed to be zero after cleared and trimmed to size");
                });
                writer.write("Testing ensureCapacity less than capacity...\n");
                writer.scope(() -> {
                    final Expected<?, Exception> expectedList = tester.expectedNewList(10);
                    expectTrue(expectedList.hasValue(),
                            "new List(10) is supposed not to throw");
                    final Object list = expectedList.getValue();
                    expectEqual(tester.capacity(list), 10,
                            "new List(10) is supposed to have capacity 10");
                    expectTrue(tester.expectedEnsureCapacity(list, 10).hasValue(),
                            "ensureCapacity(10) is supposed not to throw");
                    expectEqual(tester.capacity(list), 10,
                            "ensureCapacity(10) is supposed not to change list with capacity 10");
                    expectTrue(tester.expectedEnsureCapacity(list, 4).hasValue(),
                            "ensureCapacity(4) is supposed not to throw");
                    expectEqual(tester.capacity(list), 10,
                            "ensureCapacity(4) is supposed not to change list with capacity 10");
                    expectTrue(tester.expectedEnsureCapacity(list, 0).hasValue(),
                            "ensureCapacity(0) is supposed not to throw");
                    expectEqual(tester.capacity(list), 10,
                            "ensureCapacity(0) is supposed not to change list with capacity 10");
                });
            });
            writer.write("Testing constructor of array and toArray...\n");
            writer.scope(() -> {
                writer.write("Testing empty array...\n");
                {
                    final Object list = tester.newList(new MutableVector[0]);
                    expectTrue(tester.isEmpty(list), "new List(zero-size array) is supposed to be empty");
                    expectEqual(tester.toArray(list).length, 0,
                            "emptyList.toArray() is supposed to return empty array");
                }
                writer.write("Testing non-empty array...\n");
                {
                    final int N = 1000;
                    final MutableVector[] array = new MutableVector[N];
                    for (int i = 0; i < N; i++) {
                        array[i] = new MutableVector(random.nextDouble(-5, 5), random.nextDouble(-5, 5));
                    }
                    final Object list = tester.newList(array);
                    expectEqual(Arrays.asList(array), list, tester);
                    expectTrue(Arrays.equals(tester.toArray(list), array),
                            "new List(array).toArray() is supposed to be equal (not identical but equal) to array");
                }
            });
        });
        writer.write("Testing invalid arguments...\n");
        writer.scope(() -> {
            writer.write("Testing too great indices...\n");
            writer.scope(() -> {
                writer.write("Testing empty list indices...\n");
                {
                    final Object list = tester.newList(100);
                    expectFalse(tester.expectedGet(list, 1).hasValue(),
                            "new List(100).get(1) is supposed to throw");
                    expectFalse(tester.expectedSet(list, 13, new MutableVector(0, 0)).hasValue(),
                            "new List(100).set(13, ...) is supposed to throw");
                    expectFalse(tester.expectedRemove(list, 0).hasValue(),
                            "new List(100).remove(0, ...) is supposed to throw");
                    expectFalse(tester.expectedGet(list, 1000).hasValue(),
                            "new List(100).get(1000) is supposed to throw");
                    expectFalse(tester.expectedSet(list, 100, new MutableVector(0, 0)).hasValue(),
                            "new List(100).set(100, ...) is supposed to throw");
                    expectFalse(tester.expectedRemove(list, Integer.MAX_VALUE).hasValue(),
                            "new List(100).remove(Integer.MAX_VALUE, ...) is supposed to throw");
                }
                writer.write("Testing non-empty list indices...\n");
                {
                    final Object list = tester.newList(5);
                    tester.add(list, new MutableVector(-1, -1));
                    tester.add(list, new MutableVector(0, 0));
                    tester.add(list, new MutableVector(1, 1));
                    tester.add(list, new MutableVector(2, 2));
                    tester.add(list, new MutableVector(3, 3));
                    expectFalse(tester.expectedGet(list, 5).hasValue(),
                            "get(5) from list with size 5 is supposed to throw");
                    expectFalse(tester.expectedSet(list, 5, new MutableVector(0, 0)).hasValue(),
                            "set(5, ...) from list with size 5 is supposed to throw");
                    expectFalse(tester.expectedRemove(list, 5).hasValue(),
                            "remove(5) from list with size 5 is supposed to throw");
                    expectFalse(tester.expectedGet(list, 100).hasValue(),
                            "get(100) from list with size 5 is supposed to throw");
                    expectFalse(tester.expectedSet(list, 101, new MutableVector(0, 0)).hasValue(),
                            "set(101, ...) from list with size 5 is supposed to throw");
                    expectFalse(tester.expectedRemove(list, 103).hasValue(),
                            "remove(103) from list with size 5 is supposed to throw");
                }
                writer.write("Printing summary...\n");
                writer.scope(() -> {
                    writer.write("Setting 0 of size 0: "
                            + tester.expectedSet(tester.newList(), 0, new MutableVector(0, 0)).getError() + "\n");
                    writer.write("Getting 100 of size 0: " + tester.expectedGet(tester.newList(), 100).getError() + "\n");
                    final Object list = tester.newList();
                    tester.add(list, new MutableVector(1, 2));
                    tester.add(list, new MutableVector(3, 4));
                    tester.add(list, new MutableVector(5, 6));
                    tester.add(list, new MutableVector(7, 8));
                    writer.write("Removing 4 of size 4: " + tester.expectedRemove(list, 4).getError() + "\n");
                });
            });
            writer.write("Testing negative indices...\n");
            writer.scope(() -> {
                final Object list = tester.newList(100);
                tester.add(list, new MutableVector(-1, -1));
                tester.add(list, new MutableVector(0, 0));
                tester.add(list, new MutableVector(1, 1));
                tester.add(list, new MutableVector(2, 2));
                tester.add(list, new MutableVector(3, 3));
                tester.add(list, new MutableVector(4, 4));

                expectFalse(tester.expectedGet(list, -1).hasValue(),
                        "get(-1) is supposed to throw");
                expectFalse(tester.expectedSet(list, -2, new MutableVector(0, 0)).hasValue(),
                        "set(-2, ...) is supposed to throw");
                expectFalse(tester.expectedRemove(list, -10).hasValue(),
                        "remove(-10, ...) is supposed to throw");

                writer.write("Getting -1 of size 6: " + tester.expectedGet(list, -1).getError() + "\n");
                writer.write("Setting -2 of size 6: "
                        + tester.expectedSet(list, -2, new MutableVector(0, 0)).getError() + "\n");
                writer.write("Removing -1 of size 6: " + tester.expectedRemove(list, -10).getError() + "\n");
            });
            writer.write("Testing negative capacity...\n");
            writer.scope(() -> {
                expectFalse(tester.expectedNewList(-1).hasValue(),
                        "new List(-1) is supposed to throw");
                writer.write("new List(-1): " + tester.expectedNewList(-1).getError() + "\n");
                expectFalse(tester.expectedNewList(Integer.MIN_VALUE).hasValue(),
                        "new List(Integer.MIN_VALUE) is supposed to throw");
                writer.write("new List(Integer.MIN_VALUE): "
                        + tester.expectedNewList(Integer.MIN_VALUE).getError() + "\n");

                final Object list = tester.newList();
                expectFalse(tester.expectedEnsureCapacity(list, -1).hasValue(),
                        "ensureCapacity(-1) is supposed to throw");
                writer.write("ensureCapacity(-1): "
                        + tester.expectedEnsureCapacity(list, -1).getError() + "\n");
                expectFalse(tester.expectedEnsureCapacity(list, Integer.MIN_VALUE).hasValue(),
                        "ensureCapacity(Integer.MIN_VALUE) is supposed to throw");
                writer.write("ensureCapacity(Integer.MIN_VALUE): "
                        + tester.expectedEnsureCapacity(list, Integer.MIN_VALUE).getError() + "\n");
            });
        });
        writer.write("Testing references...\n");
        writer.scope(() -> {
            writer.write("Testing simple references...\n");
            {
                final Object list = tester.newList();
                final MutableVector vector = new MutableVector(1, 2);
                tester.add(list, vector);
                vector.setX(1000);
                expectEqual(tester.expectedGet(list, 0).getValue(), new MutableVector(1, 2),
                        "List element should not be changed after added object changed.");
                tester.expectedGet(list, 0).getValue().setY(2000);
                expectEqual(tester.expectedGet(list, 0).getValue(), new MutableVector(1, 2),
                        "List element should not be changed after got object changed.");
                vector.setX(3);
                vector.setY(4);
                tester.expectedSet(list, 0, vector);
                vector.setY(4000);
                expectEqual(tester.expectedGet(list, 0).getValue(), new MutableVector(3, 4),
                        "List element should not be changed after set object changed.");
            }
            writer.write("Testing in-array references...\n");
            {
                MutableVector[] array = new MutableVector[]{
                        new MutableVector(1, 2),
                        new MutableVector(3, 4),
                        new MutableVector(5, 6),
                        new MutableVector(7, 8),
                        new MutableVector(9, 10),
                        new MutableVector(11, 12),
                };
                final List<MutableVector> listUnchanged = Arrays.stream(array).map(MutableVector::new).toList();
                final Object list = tester.newList(array);
                array[0] = new MutableVector(0, 0);
                array[1] = new MutableVector(-1, -1);
                array[2] = new MutableVector(-2, -2);
                array[3] = new MutableVector(-3, -3);
                array[4] = new MutableVector(-4, -4);
                array[5] = new MutableVector(-5, -5);
                expectEqual(listUnchanged, list, tester);

                array = tester.toArray(list);
                array[0] = new MutableVector(0, 0);
                array[1] = new MutableVector(-1, -1);
                array[2] = new MutableVector(-2, -2);
                array[3] = new MutableVector(-3, -3);
                array[4] = new MutableVector(-4, -4);
                array[5] = new MutableVector(-5, -5);
                expectEqual(listUnchanged, list, tester);
            }
            writer.write("Testing in-array elements...\n");
            {
                MutableVector[] array = new MutableVector[]{
                        new MutableVector(1, 2),
                        new MutableVector(3, 4),
                        new MutableVector(5, 6),
                        new MutableVector(7, 8),
                        new MutableVector(9, 10),
                        new MutableVector(11, 12),
                };
                final List<MutableVector> listUnchanged = Arrays.stream(array).map(MutableVector::new).toList();
                final Object list = tester.newList(array);
                array[0].setX(0);
                array[1].setX(-1);
                array[2].setX(-2);
                array[3].setX(-3);
                array[4].setX(-4);
                array[5].setX(-5);
                expectEqual(listUnchanged, list, tester);

                array = tester.toArray(list);
                array[0].setX(0);
                array[1].setX(-1);
                array[2].setX(-2);
                array[3].setX(-3);
                array[4].setX(-4);
                array[5].setX(-5);
                expectEqual(listUnchanged, list, tester);
            }
        });
        writer.write("Testing performance...\n");
        writer.scope(() -> {
            final int N = 10000000;
            final Object list = tester.newList();
            writer.write("Adding " + N + " elements...\n");
            long addStart = System.currentTimeMillis();
            for (int i = 0; i < N; i++) {
                tester.add(list, new MutableVector(i, -i));
            }
            writer.write((System.currentTimeMillis() - addStart) / 1000.0 + " seconds elapsed\n");
            writer.write("Removing " + N + " elements from the end...\n");
            long removeStart = System.currentTimeMillis();
            for (int i = 0; i < N; i++) {
                tester.expectedRemove(list, N - i - 1);
            }
            writer.write((System.currentTimeMillis() - removeStart) / 1000.0 + " seconds elapsed\n");
        });
        writer.write("Testing memory leaks...\n");
        // NOT TO DELETE ALL MutableVectors!!!
        writer.scope(() -> {
            final ThrowingConsumer<Long, Exception> check = (expectedCount) -> {
                final long actualCount = supposeMutableVectorsCount();
                if (actualCount > expectedCount) {
                    writer.write("\nMutableVectors count ( " + actualCount + ") is greater than expected (" + expectedCount + ")\n");
                } else if (actualCount < expectedCount) {
                    writer.write("\nMutableVectors count ( " + actualCount + ") is LESS than expected (" + expectedCount + ")\n");
                } else {
                    writer.write("Success\n");
                }
            };
            writer.write("Testing removing from the middle...   ");
            final Object list = tester.newList();
            tester.add(list, new MutableVector(1, 2));
            tester.add(list, new MutableVector(3, 4));
            tester.add(list, new MutableVector(-2, -0.5));
            tester.add(list, new MutableVector(5, 5));
            tester.expectedRemove(list, 1);
            check.accept(3L);
            writer.write("Testing removing from the end...   ");
            tester.expectedRemove(list, 2);
            check.accept(2L);
            writer.write("Testing clear...   ");
            tester.clear(list);
            final MutableVector theVector = new MutableVector(0, 0);
            check.accept(1L);
            writer.write(theVector.toString().substring(0, 0)); // Doing something with theVector not to be optimized away.
        });
    }
}

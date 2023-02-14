import base.ExtendedRandom;
import base.IndentingWriter;
import base.function.ThrowingConsumer;
import mutable_vector_array_list.MutableVector;
import mutable_vector_array_list.MutableVectorArrayListTester;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        writer.write("Testing simple correctness...\n");
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

                final Object list = tester.newList(capacity);
                expectEqual(tester.size(list), 0, "Size of empty list is supposed to be 0");
                expectEqual(tester.capacity(list), capacity, "Capacity of list is supposed to be " + capacity);
                expectTrue(tester.isEmpty(list), "Empty list is supposed to be empty");
            });
            writer.write("Testing adding and valid get&set...\n");
            writer.scope(() -> {
                final int N = 1000;
                final Object list = tester.newList();

                final Function<Integer, MutableVector> addFunk = (v) -> new MutableVector(v, v + 0.5);
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
                    expectTrue(tester.get(list, i).hasValue(),
                            "Getting element with index " + i + " is not supposed to throw");
                    expectEqual(tester.get(list, i).getValue(), addFunk.apply(i),
                            "Element with index " + i + " is supposed to be equal to " + addFunk.apply(i));
                }

                final Function<Integer, MutableVector> setFunk = (v) -> new MutableVector(-v, v * -2);
                writer.write("Checking sets...\n");
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.set(list, i, setFunk.apply(i)).hasValue(),
                            "Setting element with index " + i + " is not supposed to throw");
                    expectTrue(tester.get(list, i).hasValue(),
                            "Getting element with index " + i + " is not supposed to throw");
                    expectEqual(tester.get(list, i).getValue(), setFunk.apply(i),
                            "Element with index " + i + " is supposed to be equal to " + setFunk.apply(i));
                }
            });
            writer.write("Checking no re-allocations after constructor of capacity called...\n");
            writer.scope(() -> {
                final int N = 1000;
                writer.write("Initial capacity is equal to " + N + "\n");
                final Object list = tester.newList(N);

                final Function<Integer, MutableVector> funk = (v) -> new MutableVector(Math.pow(1.25, v), Double.POSITIVE_INFINITY);

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

                final Function<Integer, MutableVector> funk = (v) -> new MutableVector(0.5 * v + 0.25, 0.5 * v);
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
                    expectTrue(tester.get(list, N - i - 1).hasValue(),
                            "Element with index " + (N - i - 1) + " is supposed to exist before deletion");
                    expectEqual(tester.get(list, N - i - 1).getValue(), funk.apply(N - i - 1),
                            "Element with index " + (N - i - 1) + " is supposed to be equal to " + funk.apply(N - i - 1));
                    final var removeResult = tester.remove(list, N - i - 1);
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

                final Function<Integer, MutableVector> funk = (v) -> new MutableVector(0.0, 1.1);
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
                    expectTrue(tester.get(list, 0).hasValue(),
                            "Element with index " + 0 + " is supposed to exist before deletion");
                    expectEqual(tester.get(list, 0).getValue(), funk.apply(i),
                            "Element with index " + 0 + " is supposed to be equal to " + funk.apply(i));
                    final var removeResult = tester.remove(list, 0);
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
                    final var removeResult = tester.remove(userList, index);
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
        });
        writer.write("Testing references...\n");
        writer.scope(() -> {
            final Object list = tester.newList();
            final MutableVector vector = new MutableVector(1, 2);
            tester.add(list, vector);
            vector.setX(1000);
            expectEqual(tester.get(list, 0).getValue(), new MutableVector(1, 2),
                    "List element should not be changed after added object changed.");
            tester.get(list, 0).getValue().setY(2000);
            expectEqual(tester.get(list, 0).getValue(), new MutableVector(1, 2),
                    "List element should not be changed after got object changed.");
            vector.setX(3);
            vector.setY(4);
            tester.set(list, 0, vector);
            vector.setY(4000);
            expectEqual(tester.get(list, 0).getValue(), new MutableVector(3, 4),
                    "List element should not be changed after set object changed.");
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
                tester.remove(list, N - i - 1);
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
            tester.remove(list, 1);
            check.accept(3L);
            writer.write("Testing removing from the end...   ");
            tester.remove(list, 2);
            check.accept(2L);
            writer.write("Testing clear...   ");
            tester.clear(list);
            final MutableVector theVector = new MutableVector(0, 0);
            check.accept(1L);
            writer.write(theVector.toString().substring(0, 0)); // Doing something with theVector not to be optimized away.
        });
    }
}

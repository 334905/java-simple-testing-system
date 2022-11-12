import base.ExtendedRandom;
import base.IndentingWriter;
import int_array_list.IntArrayListTester;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Objects;

public class IntArrayListTest {
    private static void expectTrue(final boolean cond, final String message) {
        if (!cond)
            throw new AssertionError(message);
    }

    private static void expectFalse(final boolean cond, final String message) {
        expectTrue(!cond, message);
    }

    private static void expectEqual(final Object first, final Object second, final String message) {
        expectTrue(Objects.equals(first, second), message);
    }

    public static void main(String[] args) throws Exception {
        final IntArrayListTester tester = new IntArrayListTester();
        final IndentingWriter writer = new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        final ExtendedRandom random = new ExtendedRandom();

        writer.write("Testing simple correctness...\n");
        writer.scope(() -> {
            writer.write("Testing empty IntArrayList...\n");
            writer.scope(() -> {
                final Object list = tester.newIntArrayList();
                expectEqual(tester.size(list), 0, "Size of empty IntArrayList is supposed to be 0");
                expectTrue(tester.isEmpty(list), "Empty IntArrayList is supposed to be empty");
            });
            writer.write("Testing adding 0, 1, 2, ...\n");
            writer.scope(() -> {
                final int N = 1000;
                final Object list = tester.newIntArrayList();
                for (int i = 0; i < N; i++) {
                    tester.add(list, i);
                }
                expectEqual(tester.size(list), N,
                        "Size of IntArrayList after adding " + N + " elements is supposed to be " + N);
                expectFalse(tester.isEmpty(list),
                        "IntArrayList after adding " + N + " elements is not supposed to be empty");
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.get(list, i).hasValue(),
                            "Element with index " + i + " is supposed to exist");
                    expectEqual(tester.get(list, i).getValue(), i,
                            "Element with index " + i + " is supposed to be equal to " + i);
                }
            });
            writer.write("Testing adding 1, 3, 5, ... then removing from end\n");
            writer.scope(() -> {
                final int N = 1000;
                final Object list = tester.newIntArrayList();
                for (int i = 0; i < N; i++) {
                    tester.add(list, 2 * i + 1);
                }
                for (int i = 0; i < N; i++) {
                    expectEqual(tester.size(list), N - i,
                            "Size of IntArrayList after deleting " + i + " elements is supposed to be " + (N - i));
                    expectFalse(tester.isEmpty(list),
                            "IntArrayList after deleting " + i + " elements is not supposed to be empty");
                    expectTrue(tester.get(list, N - i - 1).hasValue(),
                            "Element with index " + (N - i - 1) + " is supposed to exist");
                    expectEqual(tester.get(list, N - i - 1).getValue(), (N - i) * 2 - 1,
                            "Element with index " + (N - i - 1) + " is supposed to be equal to " + ((N - i) * 2 - 1));
                    tester.remove(list);
                }
                expectEqual(tester.size(list), 0, "Size of IntArrayList after deleting all elements is supposed to be 0");
                expectTrue(tester.isEmpty(list), "IntArrayList after deleting all elements is supposed to be empty");
            });
            writer.write("Testing random values set and creating of array...\n");
            writer.scope(() -> {
                final int N = 1000;
                int[] array = new int[N];
                final Object list = tester.ofArray(new int[N]);
                expectEqual(tester.size(list), N,
                        "Size of IntArrayList created of array of size " + N + " is supposed to be " + N);
                expectFalse(tester.isEmpty(list),
                        "IntArrayList created of array of size " + N + " is not supposed to be empty");
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.get(list, i).hasValue(),
                            "Element with index " + i + " is supposed to exist");
                    expectEqual(tester.get(list, i).getValue(), 0,
                            "All values in list of array [0, 0, ...] is supposed be zero");
                    tester.set(list, i, array[i] = random.nextInt(-100, 101));
                }
                writer.write("Values assigned are " + Arrays.toString(array));
                for (int i = 0; i < N; i++) {
                    expectTrue(tester.get(list, i).hasValue(),
                            "IntArrayList element with index " + i + " is supposed to exist");
                    expectEqual(tester.get(list, i).getValue(), array[i],
                            "IntArrayList element with index " + i + " is supposed be equal to " + array[i]);
                }
            });
            writer.write("Testing toArray...\n");
            writer.scope(() -> {
                final int[] originalArray = random.ints(1000, -1000, 1001).toArray();
                final int[] gotArray = tester.toArray(tester.ofArray(originalArray));
                expectTrue(Arrays.equals(originalArray, gotArray), "IntArrayList.ofArray(array).toArray() is supposed to produce the equal array (not necessarily the same)");
            });
        });
        writer.write("Testing performance...\n");
        writer.scope(() -> {
            final int N = 50000000;
            final Object list = tester.newIntArrayList();
            for (int i = 0; i < N; i++) {
                tester.add(list, i);
            }
            expectEqual(tester.size(list), N,
                    "Size of IntArrayList after adding " + N + " elements is supposed to be " + N);
        });
        writer.write("Testing copying...\n");
        writer.scope(() -> {
            final int[] array = new int[]{1, 2, 3, 4};
            final Object list = tester.ofArray(array);
            array[1] = 1000;
            expectEqual(tester.get(list, 1).getValue(), 2,
                    "Changing the original array is not supposed to change IntArrayList.");
            tester.set(list, 2, -1000).getValue();
            expectEqual(array[2], 3,
                    "Changing IntArrayList is not supposed to change the original array.");
        });
        writer.scope(() -> {
            final Object list = tester.ofArray(new int[]{1, 2, 3, 4});
            final int[] array = tester.toArray(list);
            array[0] = 500;
            expectEqual(tester.get(list, 0).getValue(), 1,
                    "Changing toArray array is not supposed to change the original IntArrayList.");
            tester.set(list, 3, -500).getValue();
            expectEqual(array[3], 4,
                    "Changing the original IntArrayList array is not supposed to change toArray.");
        });
        writer.write("Testing toString...\n");
        writer.scope(() -> {
            writer.write("Empty list:               " + tester.toString(tester.newIntArrayList()) + '\n');
            writer.write("List of one:              " + tester.toString(tester.ofArray(new int[]{1})) + '\n');
            writer.write("List of [1, 2, 3]:        " + tester.toString(tester.ofArray(new int[]{1, 2, 3})) + '\n');
            writer.write("List of big random array: " + tester.toString(tester.ofArray(random.ints(1000, -100, 101).toArray())) + '\n');
        });
    }
}

import base.IndentingWriter;
import base.testers.MainTester;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BinarySearchTest {
    private static class BinarySearchTester extends MainTester<int[], Void, Integer> {
        public BinarySearchTester() throws ClassNotFoundException, NoSuchMethodException {
            super("BinarySearch");
        }

        @Override
        protected List<String> convertInput(final Void input) {
            return List.of();
        }
        @Override
        protected String[] convertArgs(final int[] args) {
            return Arrays.stream(args).mapToObj(Integer::toString).toArray(String[]::new);
        }
        @Override
        protected Integer convertOutput(final List<String> output) {
            return Integer.valueOf(output.get(0));
        }

        @Override
        protected boolean checkMain(final int[] args, final Void input, final Integer output) {
            final int elem = args[0];
            final int shiftedOutput = output + 1;
            if (shiftedOutput < 1 || shiftedOutput > args.length) {
                return false;
            } else if (shiftedOutput == args.length) {
                return args.length == 1 || args[args.length - 1] < elem;
            } else {
                return args[shiftedOutput] >= elem
                        && (shiftedOutput == 1 || args[shiftedOutput - 1] < elem);
            }
        }
    }

    private static final Random random = new Random(6656202209736524691L);
    private static int[] generate(final int size, final int min, final int max) {
        return random.ints(size, min, max + 1).sorted().toArray();
    }
    private static int[] generate(final int elem, final int size, final int min, final int max) {
        return IntStream.concat(IntStream.of(elem), random.ints(size, min, max + 1).sorted()).toArray();
    }

    static final IndentingWriter writer =
            new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));

    private static String arrayToString(final int[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < Math.min(9, array.length); i++) {
            sb.append(array[i]);
            if (i + 1 != Math.min(9, array.length)) {
                sb.append(", ");
            }
        }
        if (array.length > 9) {
            sb.append("... (").append(array.length).append(" total)]");
        } else {
            sb.append("]");
        }
        return sb.toString();
    }
    private static void test(final int[] input, final BinarySearchTester tester) throws Exception {
        writer.scope(() -> {
            writer.write("Testing: " + arrayToString(input) + "\n");
            if (!tester.test(input, null)) {
                throw new AssertionError("Failure.");
            }
        });
    }

    public static void main(final String[] args) throws Exception {
        final BinarySearchTester tester = new BinarySearchTester();
        for (final int size : new int[]{6, 5, 4, 3, 2, 1, 0, 100, 10000, 500000}) {
            writer.write(String.format("Testing size %d.\n", size));
            writer.scope(() -> {
                writer.write("Testing one-value array.\n");
                writer.scope(() -> {
                    test(generate(-1, size, 0, 0), tester);
                    test(generate(0, size, 0, 0), tester);
                    test(generate(1, size, 0, 0), tester);
                });
                if (size > 0) {
                    writer.write("Testing low-range array.\n");
                    writer.scope(() -> {
                        final int mid = random.nextInt(size * 2) - size;
                        final int[] input = new int[size + 1];
                        System.arraycopy(generate(size, mid - 10, mid + 10), 0, input, 1, size);

                        input[0] = input[1] - 10;
                        test(input, tester);
                        input[0] = input[1];
                        test(input, tester);
                        input[0] = input[size / 2 + 1];
                        test(input, tester);
                        input[0] = input[size];
                        test(input, tester);
                        input[0] = input[size] + 10;
                        test(input, tester);

                        for (int i = 0; i < 50; i++) {
                            input[0] = generate(1, mid - 10, mid + 10)[0];
                            test(input, tester);
                        }
                    });
                    writer.write("Testing high-range array.\n");
                    writer.scope(() -> {
                        final int mid = random.nextInt(size * 2) - size;
                        final int[] input = new int[size + 1];
                        System.arraycopy(generate(size, mid - 10000, mid + 10000), 0, input, 1, size);

                        input[0] = input[1] - 10;
                        test(input, tester);
                        input[0] = input[1];
                        test(input, tester);
                        input[0] = input[size / 2 + 1];
                        test(input, tester);
                        input[0] = input[size];
                        test(input, tester);
                        input[0] = input[size] + 10;
                        test(input, tester);

                        for (int i = 0; i < 50; i++) {
                            input[0] = generate(1, mid - 10000, mid + 10000)[0];
                            test(input, tester);
                        }
                    });
                }
            });
        }
    }
}

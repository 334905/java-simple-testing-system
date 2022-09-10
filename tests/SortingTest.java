import base.ExtendedRandom;
import base.IndentingWriter;
import base.testers.MainTester;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SortingTest {
    private static class SortingTester extends MainTester<String[], Void, String[]> {
        public SortingTester() throws ClassNotFoundException, NoSuchMethodException {
            super("Sorting");
        }

        @Override
        protected List<String> convertInput(final Void input) {
            return List.of();
        }
        @Override
        protected String[] convertArgs(final String[] args) {
            return args;
        }
        @Override
        protected String[] convertOutput(final List<String> output) {
            return output.toArray(String[]::new);
        }

        @Override
        protected boolean checkMain(final String[] args, final Void input, final String[] output) {
            return Arrays.equals(Arrays.stream(args).sorted().toArray(), output);
        }
    }

    static final IndentingWriter writer =
            new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));

    private static <T> String arrayToString(final T[] array) {
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
    private static void test(final String[] input, final SortingTester tester) throws Exception {
        writer.scope(() -> {
            writer.write("Testing: " + arrayToString(input) + "\n");
            if (!tester.test(input, null)) {
                throw new AssertionError("Failure.");
            }
        });
    }

    public static void main(final String[] args) throws Exception {
        final ExtendedRandom random = new ExtendedRandom();
        final SortingTester tester = new SortingTester();

        for (final int size : new int[]{6, 5, 4, 3, 2, 1, 0, 100, 2000}) {
            writer.write(String.format("Testing size %d.\n", size));
            writer.scope(() -> {
                for (final int stringSize : new int[]{1, 2, 3, 4, 10, 45}) {
                    writer.write(String.format("Testing maximum string size %d.\n", stringSize));
                    writer.scope(() -> {
                        for (final String source : new String[]{"abc", "abcdefghijk", "abcdefghijklmnopqrstuvwxyz"}) {
                            writer.write(String.format("Testing letters %s.\n", arrayToString(source
                                    .chars()
                                    .mapToObj(Character::toString)
                                    .toArray(String[]::new))));
                            for (int i = 0; i < 10; i++) {
                                writer.scope(() -> {
                                    final String[] input =
                                            random.stringsFrom(1, stringSize, source, size)
                                                    .toArray(String[]::new);
                                    writer.write("Testing: " + arrayToString(input) + "\n");
                                    if (!tester.test(input, null)) {
                                        throw new AssertionError("Failure.");
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }
}

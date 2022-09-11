import base.ExtendedRandom;
import base.IndentingWriter;
import base.testers.MainTester;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SumTest {
    private static class SumTester extends MainTester<String[], Void, Integer> {
        static public final Pattern WHITESPACE = Pattern.compile("[" + Pattern.quote(SumTest.WHITESPACES) + "]");

        public SumTester() throws ClassNotFoundException, NoSuchMethodException {
            super("Sum");
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
        protected Integer convertOutput(final List<String> output) {
            return Integer.valueOf(output.get(0));
        }

        @Override
        protected boolean checkMain(final String[] args, final Void input, final Integer output) {
            return output == Arrays.stream(args)
                    .map(WHITESPACE::split)
                    .flatMap(Arrays::stream)
                    .filter(Predicate.not(String::isEmpty))
                    .mapToInt(Integer::parseInt)
                    .sum();
        }
    }

    private static final String WHITESPACES;
    private static final IndentingWriter writer =
            new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));
    private static final ExtendedRandom random = new ExtendedRandom();

    static {
        final int[] whitespaces = IntStream.rangeClosed(0, Character.MAX_VALUE).filter(Character::isWhitespace).toArray();
        WHITESPACES = new String(whitespaces, 0, whitespaces.length);
    }

    private static void test(final SumTester tester, final String... args)
            throws ReflectiveOperationException, IOException {
        writer.write("Testing: ");
        writer.write(args);
        writer.write('\n');
        if (!tester.test(args, null)) {
            throw new AssertionError("Failure.");
        }
    }

    private static void randomTests(final SumTester tester,
                                    final int size, final int minValue, final int maxValue, final int maxSpaces)
            throws ReflectiveOperationException, IOException {
        final Supplier<String> spaceStrings = random.stringsSupplierFrom(1, maxSpaces, WHITESPACES);
        test(
                tester,
                random.ints(size, minValue, maxValue)
                        .boxed()
                        .reduce(
                                new LinkedList<List<Integer>>(),
                                (lists, n) -> {
                                    if (lists.isEmpty() || random.nextBoolean()) {
                                        List<Integer> newList = new LinkedList<>();
                                        newList.add(n);
                                        lists.add(newList);
                                    } else {
                                        lists.getLast().add(n);
                                    }
                                    return lists;
                                },
                                (lists1, lists2) -> {
                                    if (lists1.isEmpty()) {
                                        return lists2;
                                    }
                                    if (lists2.isEmpty()) {
                                        return lists1;
                                    }
                                    if (!random.nextBoolean()) {
                                        lists1.getLast().addAll(lists2.getFirst());
                                        lists2.removeFirst();
                                    }
                                    lists1.addAll(lists2);
                                    return lists1;
                                })
                        .stream()
                        .map(list -> list
                                .stream()
                                .map(Object::toString)
                                .reduce((s1, s2) -> s1 + spaceStrings.get() + s2)
                                .map(s -> spaceStrings.get() + s + spaceStrings.get())
                                .orElseGet(spaceStrings))
                        .toArray(String[]::new));
    }

    public static void main(final String[] args) throws Exception {
        final SumTester tester = new SumTester();

        writer.write("Testing statement tests...\n");
        writer.scope(() -> {
            test(tester, "1", "2", "3");
            test(tester, "1", "2", "-3");
            test(tester, "1 \t2\n 3");
            test(tester, "1\t+2", "      3");
            test(tester, " ");
            test(tester, "2147483647", "1");
        });
        writer.write("Testing manual tests...\n");
        writer.scope(() -> {
            test(tester);
            test(tester, "", "\t", "\f", "\u000B", "\n ");
            test(tester, "-0000000000123440\u2029\f+000006675", "-00098890");
            test(tester, "  -02147483648  \t", "\f\r-2147483647\u2000 ", "\n\n\n-2147483646", "-00002147483645");
            test(tester, "\f\r2147483647\u2000 ", "\n\n\n+2147483646", "+00002147483645");
        });
        writer.write("Testing random tests...\n");
        writer.scope(() -> {
            for (final int size : new int[]{10, 1000, 100000}) {
                writer.write("Testing size " + size + "...\n");
                writer.scope(() -> {
                    writer.write("Testing values [-100..100]...\n");
                    writer.scope(() -> {
                        for (int i = 0; i < 10; i++) {
                            randomTests(tester, size, -100, 100, 4);
                        }
                    });
                    writer.write("Testing random values...\n");
                    writer.scope(() -> {
                        for (int i = 0; i < 10; i++) {
                            randomTests(tester, size, Integer.MIN_VALUE, Integer.MAX_VALUE, 12);
                        }
                        for (int i = 0; i < 35; i++) {
                            randomTests(tester, size, Integer.MIN_VALUE, Integer.MAX_VALUE, 75);
                        }
                    });
                });
            }
        });
    }
}

import base.ExtendedRandom;
import base.IndentingWriter;
import reverse.testers.ReverseEvenTester;
import static reverse.testers.AbstractReverseTester.IntArrayList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ReverseEvenTest {
    private static final ExtendedRandom random = new ExtendedRandom();
    private static final IndentingWriter writer =
            new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));

    private static void test(final ReverseEvenTester tester, int[][] input) throws ReflectiveOperationException, IOException {
        writer.write("Testing ");
        writer.write(Arrays.stream(input).map(Arrays::toString).toArray(String[]::new));
        writer.write('\n');
        if (!tester.test(null, input)) {
            throw new AssertionError("Failure.");
        }
    }

    private static void randomTest(final ReverseEvenTester tester,
                                   final int size, final int linesCount, final int min, final int max)
            throws ReflectiveOperationException, IOException {
        IntArrayList[] lines = new IntArrayList[linesCount];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new IntArrayList();
        }
        random.ints(size, min, max).forEach(n -> lines[random.nextInt(lines.length)].add(n));
        test(tester, Arrays.stream(lines).map(IntArrayList::toArray).toArray(int[][]::new));
    }

    public static void main(final String[] args) throws Exception {
        final ReverseEvenTester tester = new ReverseEvenTester(random);
        writer.write("Testing statement* tests...\n");
        writer.scope(() -> {
            test(tester, new int[][]{
                    {1, 2, 3, 4},
                    {5, 6}
            });
            test(tester, new int[][]{
                    {5, 6},
                    {4, 3, 2, 1}
            });
            test(tester, new int[][]{
                    {-2, 1, 10},
                    {},
                    {2, -5, -3, 4, 8}
            });
            test(tester, new int[][]{
                    {1, 2},
                    {4, 3}
            });
        });
        writer.write("Testing manual tests...\n");
        writer.scope(() -> {
            test(tester, new int[0][]);
            test(tester, new int[][]{
                    {}
            });
            test(tester, new int[][]{
                    {},
                    {},
                    {}
            });
            test(tester, new int[][]{
                    {2, 4, 6},
                    {8, 10},
                    {12}
            });
            test(tester, new int[][]{
                    {2, 4, 6},
                    {8, 10, 123, 0},
                    {},
                    {12}
            });
            test(tester, new int[][]{
                    {1},
            });
            test(tester, new int[][]{
                    {0},
            });
            test(tester, new int[][]{
                    {0},
                    {2, 8},
                    {},
                    {1},
                    {},
            });
            test(tester, new int[][]{
                    {},
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9},
                    {},
                    {10, 11, 12},
                    {},
                    {}
            });
            test(tester, new int[][]{
                    {},
                    {},
                    {},
                    {-2, 0, 1, -2, -3, 5, 6, 8, -10},
                    {},
                    {},
                    {},
                    {9, 11, 14, 13},
                    {},
                    {},
                    {},
                    {15},
                    {},
                    {},
                    {}
            });
        });
        writer.write("Testing random tests...\n");
        writer.scope(() -> {
            writer.write("Testing size 20...\n");
            writer.scope(() -> {
                randomTest(tester, 20, 10, -100, 100);
                randomTest(tester, 20, 25, -100, 100);
                randomTest(tester, 20, 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
                randomTest(tester, 20, 25, Integer.MIN_VALUE, Integer.MAX_VALUE);
            });
            writer.write("Testing size 1000...\n");
            writer.scope(() -> {
                for (int cnt : new int[]{3, 100, 1000, 10000}) {
                    randomTest(tester, 1000, cnt, Integer.MIN_VALUE, Integer.MAX_VALUE);
                }
            });
            writer.write("Testing size 100000...\n");
            writer.scope(() -> {
                randomTest(tester, 100000, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                randomTest(tester, 100000, 1000, Integer.MIN_VALUE, Integer.MAX_VALUE);
                randomTest(tester, 100000, 100000, Integer.MIN_VALUE, Integer.MAX_VALUE);
            });
        });
    }
}

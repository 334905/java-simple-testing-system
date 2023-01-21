package reverse;

import base.ExtendedRandom;
import base.IndentingWriter;
import reverse.testers.AbstractReverseTester;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BaseTest {
    protected static final ExtendedRandom random = new ExtendedRandom();
    protected static final IndentingWriter writer =
            new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));

    protected static void test(final AbstractReverseTester tester, int[][] input) throws ReflectiveOperationException, IOException {
        writer.write("Testing ");
        writer.write(Arrays.stream(input).map(Arrays::toString).toArray(String[]::new));
        writer.write('\n');
        if (!tester.test(null, input)) {
            throw new AssertionError("Failure.");
        }
    }

    protected static void randomTest(final AbstractReverseTester tester,
                                     final int size, final int linesCount, final int min, final int max)
            throws ReflectiveOperationException, IOException {
        final AbstractReverseTester.IntArrayList[] lines = new AbstractReverseTester.IntArrayList[linesCount];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new AbstractReverseTester.IntArrayList();
        }
        random.ints(size, min, max).forEach(n -> lines[random.nextInt(lines.length)].add(n));
        test(tester, Arrays.stream(lines).map(AbstractReverseTester.IntArrayList::toArray).toArray(int[][]::new));
    }
}

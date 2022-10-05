package word_stat;

import base.ExtendedRandom;
import base.IndentingWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public abstract class WordStatTestBase<S> {
    private final WordStatTesterBase<S> tester;
    private final ExtendedRandom random = new ExtendedRandom();
    protected final IndentingWriter writer =
            new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));

    protected WordStatTestBase(final WordStatTesterBase<S> tester) {
        this.tester = tester;
    }

    protected final void test(final String... args)
            throws ReflectiveOperationException, IOException {
        writer.write("Testing: ");
        writer.write(args);
        writer.write('\n');
        if (!tester.checkMain(List.of(args))) {
            throw new AssertionError("Failure.");
        }
    }

    protected final void randomTest(final int lines,
                                    final int wordsOrigin, final int wordsBound,
                                    final int lengthOrigin, final int lengthBound,
                                    final String source, final String spaceSource)
            throws ReflectiveOperationException, IOException {
        final String[] args = new String[lines];
        for (int i = 0; i < lines; i++) {
            final int size = lengthOrigin + random.nextInt(lengthBound - lengthOrigin);
            final StringBuilder sb = new StringBuilder(random.nextStringFrom(1, 10, spaceSource));
            for (int j = 0; j < size; j++) {
                sb.append(random.nextStringFrom(wordsOrigin, wordsBound, source));
                sb.append(random.nextStringFrom(1, 10, spaceSource));
            }
            args[i] = sb.toString();
        }
        test(args);
    }
}

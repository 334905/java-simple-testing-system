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
import java.util.stream.Stream;

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
}

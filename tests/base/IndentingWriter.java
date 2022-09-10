package base;

import base.function.ThrowingRunnable;
import base.function.ThrowingSupplier;

import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.regex.Pattern;

public class IndentingWriter extends Writer {
    private static final Pattern LINE_SEPARATOR = Pattern.compile("(?<=\n)");
    private int indent = 0;
    private final Writer writer;

    public IndentingWriter(final Writer writer) {
        this.writer = writer;
    }

    private void indent(final Writer writer) throws IOException {
        writer.write("  ".repeat(indent));
    }

    public <E extends Exception> void scope(final ThrowingRunnable<E> runnable) throws E {
        scope(() -> {
            runnable.run();
            return null;
        });
    }

    public <T, E extends Exception> T scope(final ThrowingSupplier<T, E> consumer) throws E {
        indent++;
        try {
            return consumer.get();
        } finally {
            indent--;
        }
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        for (String str : LINE_SEPARATOR.split(CharBuffer.wrap(cbuf, off, len))) {
            indent(writer);
            writer.write(str);
            writer.flush();
        }
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}

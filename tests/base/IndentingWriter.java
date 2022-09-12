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

    static boolean needToIndent = true;
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        for (final String str : LINE_SEPARATOR.split(CharBuffer.wrap(cbuf, off, len))) {
            if (needToIndent) {
                indent(writer);
            }
            writer.write(str);
            needToIndent = str.endsWith("\n");
            writer.flush();
        }
    }

    public <T> void write(final T[] array) throws IOException {
        final int MAX_STRING_SIZE = 30;
        final int MAX_ARRAY_SIZE = 9;

        final int printableArraySize = Math.min(MAX_ARRAY_SIZE, array.length);
        write('[');
        for (int i = 0; i < printableArraySize; i++) {
            final String objectRepresentation = array[i].toString();
            if (objectRepresentation.length() > MAX_STRING_SIZE) {
                write(objectRepresentation.substring(0, MAX_STRING_SIZE) + "...(" + objectRepresentation.length() + " chars repr)");
            } else {
                write(objectRepresentation);
            }
            if (i + 1 != array.length) {
                write(", ");
            }
        }
        if (array.length != printableArraySize) {
            write("...(" + array.length + " total)");
        }
        write("]");
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

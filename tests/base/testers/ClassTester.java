package base.testers;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class ClassTester {
    private static final ClassLoader loader;

    static {
        ClassLoader loader1;
        try {
            loader1 = new URLClassLoader(new URL[]{new File(".").toURI().toURL()});
        } catch (final MalformedURLException ignored) {
            loader1 = null;
        }
        loader = loader1;
    }

    protected final Class<?> aClass;

    public ClassTester(final String className) throws ClassNotFoundException {
        aClass = loader.loadClass(className);
    }

    protected final List<String> runMethod(final Object instance, final Method method,
                                           final Object[] args, final List<String> input)
            throws IllegalAccessException {
        final InputStream oldIn = System.in;
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] lineSeparator = System.lineSeparator().getBytes(StandardCharsets.UTF_8);
        System.setIn(
                input.stream()
                        .map(str -> (InputStream) new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)))
                        .reduce(InputStream.nullInputStream(),
                                (s1, s2) -> new SequenceInputStream(
                                        s1,
                                        new SequenceInputStream(
                                                new ByteArrayInputStream(lineSeparator),
                                                s2
                                        )
                                )
                        )
        );
        try {
            System.setOut(new PrintStream(out, false, StandardCharsets.UTF_8));
            try {
                method.invoke(instance, (Object) args);
            } catch (final InvocationTargetException e) {
                throw new AssertionError("Test failed with exception (see `Caused by`)", e.getTargetException());
            } finally {
                System.setOut(oldOut);
            }
        } finally {
            System.setIn(oldIn);
        }

        final BufferedReader outReader = new BufferedReader(new StringReader(out.toString(StandardCharsets.UTF_8)));
        return List.of(outReader.lines().toArray(String[]::new));
    }
}

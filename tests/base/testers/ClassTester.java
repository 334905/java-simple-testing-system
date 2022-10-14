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

    protected final List<String> readFile(final String fileName) throws IOException {
        try (final BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        }
    }

    protected final void writeFile(final String fileName, final List<String> lines) throws IOException {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            for (final String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    protected final void runMethod(final Object instance, final Method method, final Object... args)
            throws IllegalAccessException {
        try {
            method.invoke(instance, args);
        } catch (final InvocationTargetException e) {
            throw new AssertionError("Test failed with exception (see `Caused by`)", e.getTargetException());
        }
    }

    protected final List<String> runMethod(final Object instance, final Method method,
                                           final List<String> input, final Object... args)
            throws IllegalAccessException {
        final InputStream oldIn = System.in;
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(
                new ByteArrayInputStream(
                        input.stream().map(StringBuilder::new)
                                .reduce((s1, s2) -> s1.append(System.lineSeparator()).append(s2))
                                .map(Object::toString)
                                .orElse("")
                                .getBytes(StandardCharsets.UTF_8)
                )
        );
        try {
            System.setOut(new PrintStream(out, false, StandardCharsets.UTF_8));
            try {
                runMethod(instance, method, args);
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

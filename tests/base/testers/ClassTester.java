package base.testers;

import base.StringInputStream;
import base.either.expected.Expected;
import base.pairs.Pair;

import java.io.*;
import java.lang.reflect.Constructor;
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

    protected final Method getMethod(final Class<?> returnClass, final String methodName, final Class<?>... arguments)
            throws NoSuchMethodException {
        final Method method = aClass.getMethod(methodName, arguments);
        if (!method.getReturnType().equals(returnClass)) {
            throw new AssertionError("Your method " + method + " should have " + returnClass + " return type");
        }
        return method;
    }

    protected final <T> Expected<T, Exception> runConstructor(final Constructor<T> constructor, final Object... args)
            throws IllegalAccessException, InstantiationException {
        try {
            return Expected.ofValue(constructor.newInstance(args));
        } catch (final InvocationTargetException e) {
            return Expected.ofError(e.getTargetException());
        }
    }

    @SuppressWarnings("unchecked")
    protected final <T> Expected<T, Exception> runMethod(final Object instance, final Method method, final Object... args)
            throws IllegalAccessException {
        try {
            return Expected.ofValue((T) method.invoke(instance, args));
        } catch (final InvocationTargetException e) {
            return Expected.ofError(e.getTargetException());
        }
    }

    protected String lineSeparator() {
        return System.lineSeparator();
    }

    protected String endOfInput() {
        return "";
    }

    protected final Expected<Pair<Object, List<String>>, Exception>
    runMethod(final Object instance, final Method method,
              final List<String> input, final Object... args) throws IllegalAccessException {
        final InputStream oldIn = System.in;
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(
                new StringInputStream(
                        input.stream().map(StringBuilder::new)
                                .reduce((s1, s2) -> s1.append(lineSeparator()).append(s2))
                                .map(s -> s.append(endOfInput()))
                                .map(Object::toString)
                                .orElse(""),
                        StandardCharsets.UTF_8
                )
        );
        try {
            System.setOut(new PrintStream(out, false, StandardCharsets.UTF_8));
            try {
                final Expected<Object, Exception> runResult = runMethod(instance, method, args);
                if (runResult.hasValue()) {
                    final BufferedReader outReader = new BufferedReader(
                            new StringReader(
                                    out.toString(StandardCharsets.UTF_8)
                            )
                    );
                    return Expected.ofValue(
                            Pair.of(
                                    runResult.getValue(),
                                    List.of(outReader.lines().toArray(String[]::new))
                            )
                    );
                } else {
                    return Expected.ofError(runResult.getError());
                }
            } finally {
                System.setOut(oldOut);
            }
        } finally {
            System.setIn(oldIn);
        }
    }
}

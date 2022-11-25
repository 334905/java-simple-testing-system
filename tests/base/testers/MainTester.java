package base.testers;

import base.expected.Expected;
import base.pairs.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class MainTester<A, I, O> extends ClassTester {
    protected final Method main;

    public MainTester(final String className) throws ClassNotFoundException, NoSuchMethodException {
        super(className);
        main = aClass.getMethod("main", String[].class);
    }

    private Expected<List<String>, Exception> runMain(final String[] args, final List<String> input)
            throws IllegalAccessException {
        return runMethod(null, main, input, (Object) args).map(Pair::second);
    }

    public final boolean test(final A args, final I input) throws ReflectiveOperationException {
        List<String> output = runMain(convertArgs(args), convertInput(input)).getValue();
        return checkMain(args, input, convertOutput(output));
    }

    protected abstract List<String> convertInput(final I input);

    protected abstract String[] convertArgs(final A args);

    protected abstract O convertOutput(final List<String> output);

    protected abstract boolean checkMain(final A args, final I input, final O output);
}

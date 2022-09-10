package base.testers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class MainTester<A, I, O> extends ClassTester {
    protected final Method main;

    public MainTester(final String className) throws ClassNotFoundException, NoSuchMethodException {
        super(className);
        main = aClass.getMethod("main", String[].class);
    }

    private List<String> runMain(final String[] args, final List<String> input)
            throws InvocationTargetException, IllegalAccessException {
        return runMethod(null, main, args, input);
    }

    public final boolean test(final A args, final I input) throws ReflectiveOperationException {
        List<String> output = runMain(convertArgs(args), convertInput(input));
        if (output == null) {
            return false;
        }
        return checkMain(args, input, convertOutput(output));
    }
    protected abstract List<String> convertInput(final I input);
    protected abstract String[] convertArgs(final A args);
    protected abstract O convertOutput(final List<String> output);

    protected abstract boolean checkMain(final A args, final I input, final O output);
}

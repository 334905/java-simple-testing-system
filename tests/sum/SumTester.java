package sum;

import base.testers.MainTester;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

class SumTester<T extends Number> extends MainTester<String[], Void, T> {
    private final Mode<T> mode;
    static public final Pattern WHITESPACE;

    static {
        final int[] whitespaces = IntStream.rangeClosed(0, Character.MAX_VALUE).filter(Character::isWhitespace).toArray();
        WHITESPACE = Pattern.compile("[" + Pattern.quote(new String(whitespaces, 0, whitespaces.length)) + "]++");
    }

    public SumTester(final String className, final Mode<T> mode) throws ClassNotFoundException, NoSuchMethodException {
        super(className);
        this.mode = mode;
    }

    @Override
    protected List<String> convertInput(final Void input) {
        return List.of();
    }

    @Override
    protected String[] convertArgs(final String[] args) {
        return args;
    }

    @Override
    protected T convertOutput(final List<String> output) {
        if (output.size() != 1) {
            throw new AssertionError("Only one line should be printed.");
        }
        return mode.parseNumber(output.get(0));
    }

    @Override
    protected boolean checkMain(final String[] args, final Void input, final T output) {
        return output.equals(Arrays.stream(args)
                .map(WHITESPACE::split)
                .flatMap(Arrays::stream)
                .filter(Predicate.not(String::isEmpty))
                .map(mode::parseNumber)
                .reduce(mode::add)
                .orElseGet(mode::zero));
    }
}

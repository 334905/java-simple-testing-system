package reverse;

import base.ExtendedRandom;
import base.testers.MainTester;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractReverseTester extends MainTester<Void, int[][], int[][]> {
    public AbstractReverseTester(final String className, final ExtendedRandom random) throws ClassNotFoundException, NoSuchMethodException {
        super(className);
        this.random = random;
    }

    final ExtendedRandom random;

    @Override
    protected List<String> convertInput(final int[][] input) {
        Supplier<String> spaces = () -> random.nextStringFrom(1, 10, " ");
        return Arrays.stream(input).map(
                a -> Arrays.stream(a)
                        .mapToObj(Integer::toString)
                        .reduce((s1, s2) -> s1 + spaces.get() + s2)
                        .map(s -> spaces.get() + s + spaces.get())
                        .orElseGet(spaces)
        ).toList();
    }

    @Override
    protected String[] convertArgs(final Void args) {
        return new String[0];
    }

    @Override
    protected int[][] convertOutput(final List<String> output) {
        if (output.isEmpty()) {
            return new int[0][];
        }
        return output.stream().limit(output.size() - 1).map(
                s -> {
                    s = s.strip();
                    if (s.isEmpty()) {
                        return new int[0];
                    } else {
                        return Arrays.stream(s.split(" "))
                                .mapToInt(Integer::parseInt)
                                .toArray();
                    }
                }
        ).toArray(int[][]::new);
    }
}

package reverse;

import base.ExtendedRandom;

import java.util.Arrays;

public class ReverseEvenTester extends AbstractReverseTester {
    public ReverseEvenTester(final ExtendedRandom random) throws ClassNotFoundException, NoSuchMethodException {
        super("ReverseEven", random);
    }

    @Override
    protected boolean checkMain(final Void args, int[][] input, final int[][] output) {
        input = Arrays.stream(input).map(a -> Arrays.stream(a).filter(n -> n % 2 == 1).toArray()).toArray(int[][]::new);

        if (input.length != output.length) {
            return false;
        }
        for (int i = 0; i < input.length; i++) {
            int[] inputLine = input[i];
            int[] outputLine = output[input.length - i - 1];

            if (inputLine.length != outputLine.length) {
                return false;
            }
            for (int j = 0; j < inputLine.length; j++) {
                if (inputLine[j] != outputLine[inputLine.length - j - 1]) {
                    return false;
                }
            }
        }
        return true;
    }
}

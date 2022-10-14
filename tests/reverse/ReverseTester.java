package reverse;

import base.ExtendedRandom;

public class ReverseTester extends AbstractReverseTester {
    public ReverseTester(final ExtendedRandom random) throws ClassNotFoundException, NoSuchMethodException {
        super("Reverse", random);
    }

    @Override
    protected boolean checkMain(final Void args, final int[][] input, final int[][] output) {
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

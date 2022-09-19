package sum;

import java.util.Random;

public class ModeInteger implements Mode<Integer> {
    @Override
    public Integer parseNumber(final String str) {
        return Integer.parseInt(str);
    }

    @Override
    public Integer zero() {
        return 0;
    }

    @Override
    public Integer add(final Integer left, final Integer right) {
        return left + right;
    }

    @Override
    public Integer random(final Random rng, final Integer origin, final Integer bound) {
        return rng.ints(1, origin, bound).findFirst().getAsInt();
    }

    static public final ModeInteger instance = new ModeInteger();
}

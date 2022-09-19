package sum;

import java.math.BigInteger;
import java.util.Random;

public class ModeBigInteger implements Mode<BigInteger> {
    @Override
    public BigInteger parseNumber(final String str) {
        return new BigInteger(str);
    }

    @Override
    public BigInteger zero() {
        return BigInteger.ZERO;
    }

    @Override
    public BigInteger add(final BigInteger left, final BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger random(final Random rng, final BigInteger origin, final BigInteger bound) {
        final BigInteger distance = bound.subtract(origin);
        BigInteger answer;
        do {
            answer = new BigInteger(distance.bitLength(), rng);
        } while (answer.compareTo(distance) >= 0);
        return answer.add(origin);
    }

    static public final ModeBigInteger instance = new ModeBigInteger();
}

package base;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ExtendedRandom extends Random {
    public ExtendedRandom() {
        super(6656202209736524691L);
    }
    public ExtendedRandom(final long seed) {
        super(seed);
    }

    public char nextCharFrom(final String source) {
        return source.charAt(nextInt(source.length()));
    }

    public <T> T nextElementFrom(final T[] array) {
        return nextElementFrom(Arrays.asList(array));
    }

    public <T> T nextElementFrom(final List<T> list) {
        return list.get(nextInt(list.size()));
    }

    public String nextStringFrom(final int size, final String source) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(nextCharFrom(source));
        }
        return sb.toString();
    }

    // Max inclusively.
    public String nextStringFrom(final int minSize, final int maxSize, final String source) {
        return nextStringFrom(nextInt(maxSize - minSize + 1) + minSize, source);
    }

    public Supplier<String> stringsSupplierFrom(final int minSize, final int maxSize, final String source) {
        return () -> nextStringFrom(minSize, maxSize, source);
    }

    public Stream<String> stringsFrom(final int minSize, final int maxSize, final String source) {
        return Stream.generate(stringsSupplierFrom(minSize, maxSize, source));
    }

    public Stream<String> stringsFrom(final int minSize, final int maxSize, final String source, final long size) {
        return stringsFrom(minSize, maxSize, source).limit(size);
    }

    public BigInteger nextBigInteger(final BigInteger bound) {
        BigInteger result = new BigInteger(bound.bitLength(), this);
        while (result.compareTo(bound) >= 0) {
            result = new BigInteger(bound.bitLength(), this);
        }
        return result;
    }

    public BigInteger nextBigInteger(final BigInteger origin, final BigInteger bound) {
        final BigInteger delta = bound.subtract(origin);
        return nextBigInteger(delta).add(origin);
    }
}

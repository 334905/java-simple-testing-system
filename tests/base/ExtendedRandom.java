package base;

import java.util.Random;
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

    public Stream<String> stringsFrom(final int minSize, final int maxSize, final String source) {
        return Stream.generate(() -> nextStringFrom(minSize, maxSize, source));
    }

    public Stream<String> stringsFrom(final int minSize, final int maxSize, final String source, final long size) {
        return stringsFrom(minSize, maxSize, source).limit(size);
    }
}

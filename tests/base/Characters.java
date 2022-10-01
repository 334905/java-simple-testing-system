package base;

import java.nio.CharBuffer;
import java.util.stream.IntStream;

public class Characters {
    public static final String WHITESPACES;

    static {
        final int[] whitespaces = IntStream.rangeClosed(0, Character.MAX_VALUE).filter(Character::isWhitespace).toArray();
        WHITESPACES = new String(whitespaces, 0, whitespaces.length);
    }

    public static final String DECIMAL_NUMBERS;
    public static final CharBuffer[] DECIMALS = new CharBuffer[10];

    static {
        final String[] decimals = new String[10];
        final StringBuilder[] builders = new StringBuilder[decimals.length];
        for (int i = 0; i < builders.length; i++) {
            builders[i] = new StringBuilder();
        }
        IntStream.rangeClosed(0, Character.MAX_VALUE)
                .filter(ch -> Character.getType(ch) == Character.DECIMAL_DIGIT_NUMBER)
                .forEach(ch -> builders[Integer.parseInt("" + (char) ch)].append((char) ch));
        for (int i = 0; i < decimals.length; i++) {
            decimals[i] = builders[i].toString();
        }
        DECIMAL_NUMBERS = String.join("", decimals);

        for (int i = 0; i < 10; i++) {
            //DECIMALS[i] = CharBuffer.wrap(DECIMAL_NUMBERS,
        }
    }
}

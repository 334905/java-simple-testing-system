package base;

import java.nio.CharBuffer;
import java.util.stream.IntStream;

public class Characters {
    public static final String WHITESPACES;

    static {
        final int[] whitespaces = IntStream.rangeClosed(0, Character.MAX_VALUE).filter(Character::isWhitespace).toArray();
        WHITESPACES = new String(whitespaces, 0, whitespaces.length);
    }

    public static final String[] DECIMALS = new String[10];

    static {
        final StringBuilder[] builders = new StringBuilder[DECIMALS.length];
        for (int i = 0; i < builders.length; i++) {
            builders[i] = new StringBuilder();
        }
        IntStream.rangeClosed(0, Character.MAX_VALUE)
                .filter(ch -> Character.getType(ch) == Character.DECIMAL_DIGIT_NUMBER)
                .forEach(ch -> builders[Integer.parseInt("" + (char) ch)].append((char) ch));
        for (int i = 0; i < DECIMALS.length; i++) {
            DECIMALS[i] = builders[i].toString();
        }
    }

    public static final String LETTERS;

    static {
        final int[] letters = IntStream.rangeClosed(0, Character.MAX_VALUE)
                .filter(Character::isLetter)
                .toArray();
        LETTERS = new String(letters, 0, letters.length);
    }

    public static final String LETTERS_ENGLISH_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String LETTERS_ENGLISH_UPPER = LETTERS_ENGLISH_LOWER.toUpperCase();
    public static final String LETTERS_ENGLISH = LETTERS_ENGLISH_UPPER + LETTERS_ENGLISH_LOWER;

    public static final String LETTERS_RUSSIAN_LOWER = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    public static final String LETTERS_RUSSIAN_UPPER = LETTERS_RUSSIAN_LOWER.toUpperCase();
    public static final String LETTERS_RUSSIAN = LETTERS_RUSSIAN_UPPER + LETTERS_RUSSIAN_LOWER;

    public static final String LETTERS_GREEK_LOWER = "αβγδεζηθικλμνξοπρςστυφχψω";
    public static final String LETTERS_GREEK_UPPER = LETTERS_GREEK_LOWER.toUpperCase();
    public static final String LETTERS_GREEK = LETTERS_GREEK_UPPER + LETTERS_GREEK_LOWER;
}

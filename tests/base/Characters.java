package base;

import base.pairs.Pair;

import java.util.stream.IntStream;

public class Characters {
    public static final String WHITESPACES;

    static {
        final int[] whitespaces = IntStream.rangeClosed(0, Character.MAX_VALUE).filter(Character::isWhitespace).toArray();
        WHITESPACES = new String(whitespaces, 0, whitespaces.length);
    }

    public static final String[] DIGITS = new String[Character.MAX_RADIX];

    static {
        final StringBuilder[] builders = new StringBuilder[DIGITS.length];
        for (int i = 0; i < builders.length; i++) {
            builders[i] = new StringBuilder();
        }
        IntStream.rangeClosed(0, Character.MAX_VALUE)
                .mapToObj(c -> Pair.of(c, Character.digit(c, Character.MAX_RADIX)))
                .filter(pair -> pair.second() != -1)
                .forEachOrdered(pair -> builders[pair.second()].append((char) pair.first().intValue()));
        for (int i = 0; i < DIGITS.length; i++) {
            DIGITS[i] = builders[i].toString();
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

    public static final String LETTERS_RUSSIAN_LOWER = "\u0430\u0431\u0432\u0433\u0434\u0435\u0451\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044a\u044b\u044c\u044d\u044e\u044f";
    public static final String LETTERS_RUSSIAN_UPPER = LETTERS_RUSSIAN_LOWER.toUpperCase();
    public static final String LETTERS_RUSSIAN = LETTERS_RUSSIAN_UPPER + LETTERS_RUSSIAN_LOWER;

    public static final String LETTERS_GREEK_LOWER = "\u03b1\u03b2\u03b3\u03b4\u03b5\u03b6\u03b7\u03b8\u03b9\u03ba\u03bb\u03bc\u03bd\u03be\u03bf\u03c0\u03c1\u03c2\u03c3\u03c4\u03c5\u03c6\u03c7\u03c8\u03c9";
    public static final String LETTERS_GREEK_UPPER = LETTERS_GREEK_LOWER.toUpperCase();
    public static final String LETTERS_GREEK = LETTERS_GREEK_UPPER + LETTERS_GREEK_LOWER;

    public static final String DASHES;

    static {
        final int[] dashes = IntStream.rangeClosed(0, Character.MAX_VALUE)
                .filter(ch -> Character.getType(ch) == Character.DASH_PUNCTUATION)
                .toArray();
        DASHES = new String(dashes, 0, dashes.length);
    }
}

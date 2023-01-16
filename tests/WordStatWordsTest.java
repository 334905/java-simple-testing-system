import base.Characters;
import word_stat.WordStatTestBase;
import word_stat.WordStatTesterBase;
import word_stat.stats.WordAndCount;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WordStatWordsTest extends WordStatTestBase<WordAndCount> {
    protected WordStatWordsTest() throws ClassNotFoundException, NoSuchMethodException {
        super(new WordStatWordsTester());
    }

    private static class WordStatWordsTester extends WordStatTesterBase<WordAndCount> {
        public WordStatWordsTester() throws ClassNotFoundException, NoSuchMethodException {
            super("WordStatWords", Pattern.compile("(?:'|\\p{L}|\\p{Pd})++"));
        }

        @Override
        protected List<WordAndCount> collectStats(final Stream<Word> words) {
            return words.collect(Collectors.groupingBy(w -> w.word().toLowerCase(), Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> new WordAndCount(e.getKey(), e.getValue()))
                    .toList();
        }

        @Override
        protected boolean checkStat(final String outputLine, final WordAndCount stat) {
            return outputLine.equals(stat.word() + " " + stat.count());
        }
    }

    private static final String CHARACTERS = "'" + Characters.DASHES + Characters.LETTERS;
    private static final String NOT_CHARACTERS;

    static {
        int[] not_chars = IntStream.rangeClosed(0, Character.MAX_VALUE)
                .filter(c -> (c < '\uD800' || c > '\uDFFF') && !Character.isLetter(c) && Character.getType(c) != Character.DASH_PUNCTUATION && c != '\'')
                .toArray();
        NOT_CHARACTERS = new String(not_chars, 0, not_chars.length);
    }

    private void main() throws ReflectiveOperationException, IOException {
        test("To be, or not to be, that is the question:");
        test("Monday's child is fair of face.", "Tuesday's child is full of grace.");
        // test("Шалтай-Болтай", "Сидел на стене.", "Шалтай-Болтай", "Свалился во сне.");
        test("\u0428\u0430\u043b\u0442\u0430\u0439-\u0411\u043e\u043b\u0442\u0430\u0439",
                "\u0421\u0438\u0434\u0435\u043b \u043d\u0430 \u0441\u0442\u0435\u043d\u0435.",
                "\u0428\u0430\u043b\u0442\u0430\u0439-\u0411\u043e\u043b\u0442\u0430\u0439",
                "\u0421\u0432\u0430\u043b\u0438\u043b\u0441\u044f \u0432\u043e \u0441\u043d\u0435.");
        // test("兵 者, 诡 道 也. 故 能 而 示 之 不能, 用 而 示 之 不用, 近 而 示 之 远, 远 而 示 之 近");
        test("\u5175 \u8005, \u8be1 \u9053 \u4e5f. \u6545 \u80fd \u800c \u793a \u4e4b \u4e0d\u80fd, \u7528 \u800c \u793a \u4e4b \u4e0d\u7528, \u8fd1 \u800c \u793a \u4e4b \u8fdc, \u8fdc \u800c \u793a \u4e4b \u8fd1");
        test("\t\t\t", " \t ", "", "\t");
        // test("\t σω   ςα\t \tςα   ςα\tσω  ");
        test("\t\u03c3\u03c9   \u03c2\u03b1\t \t\u03c2\u03b1   \u03c2\u03b1\t\u03c3\u03c9");

        randomTest(10, 1, 3, 1, 2, Characters.LETTERS_ENGLISH + Characters.DASHES + "'", " ");
        randomTest(15, 1, 3, 1, 20, Characters.LETTERS_RUSSIAN + Characters.DASHES + "'", ",.;:!\"?()[]{} \t");
        randomTest(35, 1, 4, 1, 50, CHARACTERS, ",.;:!\"?()[]{} \t\u00B0");
        randomTest(100000, 1, 50, 1, 5, CHARACTERS, NOT_CHARACTERS);
        randomTest(10000, 1, 50, 1, 25, CHARACTERS, NOT_CHARACTERS);
        System.out.println("Tests passed.");
    }

    public static void main(final String[] args) throws ReflectiveOperationException, IOException {
        new WordStatWordsTest().main();
    }
}

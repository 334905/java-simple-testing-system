import base.Characters;
import word_stat.WordStatTestBase;
import word_stat.WordStatTesterBase;
import word_stat.stats.WordAndCount;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordStatLexicalTest extends WordStatTestBase<WordAndCount> {
    protected WordStatLexicalTest() throws ClassNotFoundException, NoSuchMethodException {
        super(new WordStatLexicalTester());
    }

    private static class WordStatLexicalTester extends WordStatTesterBase<WordAndCount> {
        public WordStatLexicalTester() throws ClassNotFoundException, NoSuchMethodException {
            super("WordStatLexical", Pattern.compile("[^" + Pattern.quote(Characters.WHITESPACES) + "]++"));
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

        randomTest(10, 1, 3, 1, 2, Characters.LETTERS_ENGLISH, " ");
        randomTest(15, 1, 3, 1, 20, Characters.LETTERS_RUSSIAN, " \t");
        randomTest(35, 1, 4, 1, 50, Characters.LETTERS_GREEK + Characters.LETTERS_RUSSIAN, " \t");
        randomTest(100,
                1, 5,
                1, 50,
                Characters.LETTERS_GREEK
                        + Characters.LETTERS_RUSSIAN
                        + Characters.LETTERS_ENGLISH
                        + Characters.DIGITS[0] + Characters.DIGITS[9]
                        + "~!@#$%^&*()<>?:\"{}|[]\\;',./",
                " \t\u00B0");
    }

    public static void main(final String[] args) throws ReflectiveOperationException, IOException {
        new WordStatLexicalTest().main();
    }
}

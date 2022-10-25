import base.Characters;
import word_stat.WordStatTestBase;
import word_stat.WordStatTesterBase;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordStatLexicalTest extends WordStatTestBase<WordStatLexicalTest.WordAndCount> {
    protected WordStatLexicalTest() throws ClassNotFoundException, NoSuchMethodException {
        super(new WordStatLexicalTester());
    }

    protected static record WordAndCount(String word, long count) {
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

    public void main() throws ReflectiveOperationException, IOException {
        test("To be, or not to be, that is the question:");
        test("Monday's child is fair of face.", "Tuesday's child is full of grace.");
        test("Шалтай-Болтай", "Сидел на стене.", "Шалтай-Болтай", "Свалился во сне.");
        test("兵 者, 诡 道 也. 故 能 而 示 之 不能, 用 而 示 之 不用, 近 而 示 之 远, 远 而 示 之 近");
        test("\t\t\t", " \t ", "", "\t");
        test("\t σω   ςα\t \tςα   ςα\tσω  ");

        randomTest(10, 1, 3, 1, 2, Characters.LETTERS_ENGLISH, " ");
        randomTest(15, 1, 3, 1, 20, Characters.LETTERS_RUSSIAN, " \t");
        randomTest(35, 1, 4, 1, 50, Characters.LETTERS_GREEK + Characters.LETTERS_RUSSIAN, " \t");
        randomTest(100,
                1, 5,
                1, 50,
                Characters.LETTERS_GREEK
                        + Characters.LETTERS_RUSSIAN
                        + Characters.LETTERS_ENGLISH
                        + Characters.DECIMALS[0] + Characters.DECIMALS[9]
                        + "~!@#$%^&*()<>?:\"{}|[]\\;',./",
                " \t\u00B0");
    }

    public static void main(final String[] args) throws ReflectiveOperationException, IOException {
        new WordStatLexicalTest().main();
    }
}

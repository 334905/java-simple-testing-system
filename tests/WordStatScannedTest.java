import base.Characters;
import base.pairs.DualCollector;
import word_stat.WordStatTestBase;
import word_stat.WordStatTesterBase;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordStatScannedTest extends WordStatTestBase<WordStatScannedTest.WordAndCount> {
    protected WordStatScannedTest() throws ClassNotFoundException, NoSuchMethodException {
        super(new WordStatScannedTester());
    }

    record WordAndCount(String word, long count) {
    }

    private static class WordStatScannedTester extends WordStatTesterBase<WordAndCount> {
        public WordStatScannedTester() throws ClassNotFoundException, NoSuchMethodException {
            super("WordStatScanned", Pattern.compile("[^" + Pattern.quote(Characters.WHITESPACES) + "]++"));
        }

        @Override
        protected List<WordAndCount> collectStats(final Stream<Word> words) {
            return words.collect(
                            Collectors.groupingBy(w -> w.word().toLowerCase(),
                                    DualCollector.of(
                                            Collectors.counting(),
                                            Collectors.collectingAndThen(
                                                    Collectors.minBy(Comparator.comparingLong(Word::wordNo)),
                                                    o -> o.map(Word::wordNo).orElse(0L)
                                            )
                                    )
                            )
                    ).entrySet().stream()
                    .sorted(Comparator.comparingLong(e -> e.getValue().second()))
                    .map(wcp -> new WordAndCount(wcp.getKey(), wcp.getValue().first()))
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

       randomTest(10, 0, 5, 1, 20, Characters.LETTERS_ENGLISH, " ");
       randomTest(15, 3, 15, 1, 20, Characters.LETTERS_RUSSIAN, " \t");
       randomTest(35, 10, 30, 1, 50, Characters.LETTERS_GREEK + Characters.LETTERS_RUSSIAN, " \t");
       randomTest(100,
               0, 50,
               1, 50,
               Characters.LETTERS_GREEK
                       + Characters.LETTERS_RUSSIAN
                       + Characters.LETTERS_ENGLISH
                       + Characters.DECIMALS[0] + Characters.DECIMALS[9]
                       + "~!@#$%^&*()<>?:\"{}|[]\\;',./",
               " \t\u00B0");
    }

    public static void main(final String[] args) throws ReflectiveOperationException, IOException {
        new WordStatScannedTest().main();
    }
}

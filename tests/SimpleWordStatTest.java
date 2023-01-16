import base.pairs.DualCollector;
import base.pairs.Pair;
import word_stat.WordStatTestBase;
import word_stat.WordStatTesterBase;
import word_stat.stats.WordAndCount;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleWordStatTest extends WordStatTestBase<WordAndCount> {
    private static class SimpleWordStatTester extends WordStatTesterBase<WordAndCount> {
        public SimpleWordStatTester() throws ClassNotFoundException, NoSuchMethodException {
            super("SimpleWordStat",
                    Pattern.compile("[^" + Pattern.quote(" \t\n\u000B\f\r") + "]++"));
        }

        @Override
        protected List<WordAndCount> collectStats(final Stream<Word> words) {
            final Map<String, Long> counts = words.collect(
                    Collectors.groupingBy(w -> w.word().toLowerCase(), Collectors.counting())
            );
            return counts.entrySet().stream()
                    .map(e -> new WordAndCount(e.getKey(), e.getValue().intValue()))
                    .toList();
        }

        @Override
        protected boolean checkStat(final String outputLine, final WordAndCount stat) {
            return outputLine.equals(stat.word() + " " + stat.count());
        }

        @Override
        protected boolean checkStats(final List<String> output, final List<WordAndCount> stats) {
            return stats.stream()
                    .map(stat -> stat.word() + " " + stat.count())
                    .collect(Collectors.toUnmodifiableSet())
                    .equals(Set.copyOf(output));
        }
    }

    public SimpleWordStatTest() throws ClassNotFoundException, NoSuchMethodException {
        super(new SimpleWordStatTester());
    }

    public void main() throws ReflectiveOperationException, IOException {
        test("To be, or not to be, that is the question:");
        test("Monday's child is fair of face.", "Tuesday's child is full of grace.");
        test("Шалтай-Болтай", "Сидел на стене.", "Шалтай-Болтай", "Свалился во сне.");
        test("兵 者, 诡 道 也. 故 能 而 示 之 不能, 用 而 示 之 不用, 近 而 示 之 远, 远 而 示 之 近");
    }

    public static void main(final String[] args) throws ReflectiveOperationException, IOException {
        new SimpleWordStatTest().main();
    }
}

import base.Characters;
import word_stat.WordStatTestBase;
import word_stat.WordStatTesterBase;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordStatLexicalTest extends WordStatTestBase<WordStatLexicalTest.WordAndCount> {
    protected WordStatLexicalTest() throws ClassNotFoundException, NoSuchMethodException {
        super(new WordStatLexicalTester());
    }

    protected static final class WordAndCount {
        private final String word;
        private final long count;

        private WordAndCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        public String word() {
            return word;
        }

        public long count() {
            return count;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (WordAndCount) obj;
            return Objects.equals(this.word, that.word) &&
                    this.count == that.count;
        }

        @Override
        public int hashCode() {
            return Objects.hash(word, count);
        }

        @Override
        public String toString() {
            return "WordAndCount[" +
                    "word=" + word + ", " +
                    "count=" + count + ']';
        }

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
        // test("????????????-????????????", "?????????? ???? ??????????.", "????????????-????????????", "???????????????? ???? ??????.");
        test("\u0428\u0430\u043b\u0442\u0430\u0439-\u0411\u043e\u043b\u0442\u0430\u0439",
                "\u0421\u0438\u0434\u0435\u043b \u043d\u0430 \u0441\u0442\u0435\u043d\u0435.",
                "\u0428\u0430\u043b\u0442\u0430\u0439-\u0411\u043e\u043b\u0442\u0430\u0439",
                "\u0421\u0432\u0430\u043b\u0438\u043b\u0441\u044f \u0432\u043e \u0441\u043d\u0435.");
        // test("??? ???, ??? ??? ???. ??? ??? ??? ??? ??? ??????, ??? ??? ??? ??? ??????, ??? ??? ??? ??? ???, ??? ??? ??? ??? ???");
        test("\u5175 \u8005, \u8be1 \u9053 \u4e5f. \u6545 \u80fd \u800c \u793a \u4e4b \u4e0d\u80fd, \u7528 \u800c \u793a \u4e4b \u4e0d\u7528, \u8fd1 \u800c \u793a \u4e4b \u8fdc, \u8fdc \u800c \u793a \u4e4b \u8fd1");
        test("\t\t\t", " \t ", "", "\t");
        // test("\t ????   ????\t \t????   ????\t????  ");
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
                        + Characters.DECIMALS[0] + Characters.DECIMALS[9]
                        + "~!@#$%^&*()<>?:\"{}|[]\\;',./",
                " \t\u00B0");
    }

    public static void main(final String[] args) throws ReflectiveOperationException, IOException {
        new WordStatLexicalTest().main();
    }
}

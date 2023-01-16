package word_stat.stats;

import java.util.Objects;

public class WordAndCount {
    private final String word;
    private final long count;

    public WordAndCount(String word, long count) {
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
        /*return "WordAndCount[" +
                "word=" + word + ", " +
                "count=" + count + ']';*/
        return word + " " + count;
    }
}

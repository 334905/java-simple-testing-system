package word_stat;

import base.testers.ClassTester;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class WordStatTesterBase<S> extends ClassTester {
    private final Method main;
    private final Pattern wordPattern;

    protected record Word(String word, long wordNo, long lineNo, long wordNoInLine) {}

    public WordStatTesterBase(final String className, final Pattern pattern)
            throws ClassNotFoundException, NoSuchMethodException {
        super(className);
        main = aClass.getMethod("main", String[].class);
        wordPattern = pattern;
    }

    private List<String> runMain(final String inputFileName, final String outputFileName, final List<String> input)
            throws IOException, IllegalAccessException {
        writeFile(inputFileName, input);
        runMethod(null, main, (Object) new String[]{inputFileName, outputFileName});
        return readFile(outputFileName);
    }

    private List<S> collectStats(final List<String> input) {
        final List<Word> words = new ArrayList<>();
        long wordNo = 0;
        for (int lineNo = 0; lineNo < input.size(); lineNo++) {
            final Matcher matcher = wordPattern.matcher(input.get(lineNo));
            long wordNoInLine = 0;
            while (matcher.find()) {
                words.add(new Word(matcher.group(), wordNo++, lineNo, wordNoInLine++));
            }
        }
        return collectStats(words.stream());
    }

    protected abstract List<S> collectStats(final Stream<Word> words);
    protected abstract boolean checkStat(final String outputLine, final S stat);
    protected boolean checkStats(final List<String> output, final List<S> stats) {
        if (stats.size() != output.size()) {
            throw new AssertionError("Expected " + stats.size() + " stats entries, found " + output.size());
        }

        for (int i = 0; i < stats.size(); i++) {
            if (!checkStat(output.get(i), stats.get(i))) {
                return false;
            }
        }
        return true;
    }

    public final boolean checkMain(final String inputFileName, final String outputFileName, final List<String> input)
            throws IOException, IllegalAccessException {
        final List<S> stats = collectStats(input);
        final List<String> output = runMain(inputFileName, outputFileName, input);

        return checkStats(output, stats);
    }
    public final boolean checkMain(final List<String> input)
            throws IOException, IllegalAccessException {
        return checkMain("input.txt", "output.txt", input);
    }
}

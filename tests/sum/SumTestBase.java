package sum;

import base.ExtendedRandom;
import base.IndentingWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SumTestBase<T extends Number> {
    private static final String WHITESPACES;

    static {
        final int[] whitespaces = IntStream.rangeClosed(0, Character.MAX_VALUE).filter(Character::isWhitespace).toArray();
        WHITESPACES = new String(whitespaces, 0, whitespaces.length);
    }


    private static final String[] DECIMALS = new String[10];

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

    private final Mode<T> mode;
    private final SumTester<T> tester;
    private final ExtendedRandom random = new ExtendedRandom();
    protected final IndentingWriter writer =
            new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));

    public SumTestBase(final String className, final Mode<T> mode)
            throws ClassNotFoundException, NoSuchMethodException {
        this.mode = mode;
        tester = new SumTester<>(className, mode);
    }

    protected void test(final String... args)
            throws ReflectiveOperationException, IOException {
        writer.write("Testing: ");
        writer.write(args);
        writer.write('\n');
        if (!tester.test(args, null)) {
            throw new AssertionError("Failure.");
        }
    }

    protected void randomTests(final int size, final T origin, final T bound, final int maxSpaces)
            throws ReflectiveOperationException, IOException {
        final Supplier<String> spaceStrings = random.stringsSupplierFrom(1, maxSpaces, WHITESPACES);
        final Supplier<T> randomSupplier = () -> mode.random(random, origin, bound);
        test(Stream.generate(randomSupplier)
                .limit(size)
                .reduce(
                        new LinkedList<List<T>>(),
                        (lists, n) -> {
                            if (lists.isEmpty() || random.nextBoolean()) {
                                List<T> newList = new LinkedList<>();
                                newList.add(n);
                                lists.add(newList);
                            } else {
                                lists.getLast().add(n);
                            }
                            return lists;
                        },
                        (lists1, lists2) -> {
                            if (lists1.isEmpty()) {
                                return lists2;
                            }
                            if (lists2.isEmpty()) {
                                return lists1;
                            }
                            if (!random.nextBoolean()) {
                                lists1.getLast().addAll(lists2.getFirst());
                                lists2.removeFirst();
                            }
                            lists1.addAll(lists2);
                            return lists1;
                        })
                .stream()
                .map(list -> list
                        .stream()
                        .map(Object::toString)
                        .map(s -> {
                            if (s.startsWith("-")) {
                                return "-" + "0".repeat(random.nextInt(10)) + s.substring(1);
                            } else {
                                return (random.nextBoolean() ? "+" : "") + "0".repeat(random.nextInt(10)) + s;
                            }
                        })
                        .map(s -> s.chars()
                                .map(ch -> ch >= '0' && ch <= '9' ? random.nextCharFrom(DECIMALS[ch - '0']) : ch)
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append))
                        .reduce((s1, s2) -> s1.append(spaceStrings.get()).append(s2))
                        .map(s -> spaceStrings.get() + s + spaceStrings.get())
                        .orElseGet(spaceStrings))
                .toArray(String[]::new));
    }


}


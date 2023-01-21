package reverse.testers;

import base.Characters;
import base.ExtendedRandom;
import base.pairs.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ReverseHexTester extends AbstractReverseTester {
    public ReverseHexTester(final ExtendedRandom random) throws ClassNotFoundException, NoSuchMethodException {
        super("ReverseHex", random);
    }

    @SuppressWarnings("unchecked")
    private static final Pair<Integer, String[]>[] RADIXES = new Pair[]{
            Pair.of(10, new String[]{""}),
            Pair.of(16, new String[]{"0x", "0X"})
    };

    private StringBuilder addInteger(final StringBuilder sb, final int value) {
        final Pair<Integer, String[]> radix = random.nextElementFrom(RADIXES);

        if (value == 0) {
            sb.append(random.nextElementFrom(new String[]{"+", "-", ""}));
        } else if (value > 0) {
            sb.append(random.nextElementFrom(new String[]{"+", ""}));
        } else {
            sb.append('-');
        }
        sb.append(random.nextElementFrom(radix.second()));
        sb.append(random.nextStringFrom(0, 3, Characters.DIGITS[0]));
        Long.toString(Math.abs((long) value), radix.first()).chars()
                .map(ch -> random.nextCharFrom(Characters.DIGITS[Character.digit(ch, radix.first())]))
                .forEachOrdered(ch -> sb.append((char) ch));

        return sb;
    }

    private static final String NON_CR_NOR_LF_WHITESPACES;
    static {
        final StringBuilder sb = new StringBuilder();
        Characters.WHITESPACES.chars()
                .filter(ch -> ch != '\n' && ch != '\r')
                .forEachOrdered(ch -> sb.append((char) ch));
        NON_CR_NOR_LF_WHITESPACES = sb.toString();
    }

    @Override
    protected List<String> convertInput(final int[][] input) {
        final Supplier<String> spaces = () -> random.nextStringFrom(1, 10, NON_CR_NOR_LF_WHITESPACES);
        return Arrays.stream(input).map(
                line -> {
                    final StringBuilder sb = new StringBuilder(spaces.get());
                    for (final int val : line) {
                        addInteger(sb, val).append(spaces.get());
                    }
                    return sb.toString();
                }).toList();
    }
}

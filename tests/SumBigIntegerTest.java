import sum.Mode;
import sum.ModeBigInteger;
import sum.SumTestBase;

import java.math.BigInteger;

public class SumBigIntegerTest extends SumTestBase<BigInteger> {
    public SumBigIntegerTest() throws ClassNotFoundException, NoSuchMethodException {
        super("SumBigInteger", ModeBigInteger.instance);
    }

    private void run() throws Exception {
        writer.write("Testing statement tests...\n");
        writer.scope(() -> {
            writer.write("Testing small integers...\n");
            writer.scope(() -> {
                test("1", "2", "3");
                test("1", "2", "-3");
                test("1 2 3");
                test("1 +2", "      3");
                test(" ");
            });
            writer.write("Testing big integers...\n");
            writer.scope(() -> {
                test("2147483647", "1");
                test("9223372036854775808", "+1");
                test("-9223372036854775809 -9223372036854775808");
                test("340282366920938463463374607431768211456");
            });
        });
        writer.write("Testing manual tests with 0 to 9 digits...\n");
        writer.scope(() -> {
            writer.write("Testing small integers...\n");
            writer.scope(() -> {
                test("\f-1\t2\n+3\r");
                test();
                test("", "\t", "\f", "\u000B", "\n ");
                test("-0000000000123440\u2029\f+000006675", "-00098890");
            });
            writer.write("Testing big integers...\n");
            writer.scope(() -> {
                test("  -02147483648  \t", "\f\r-2147483647\u2000 ", "\n\n\n-2147483646", "-00002147483645");
                test("\f\r2147483647\u2000 ", "\n\n\n+2147483646", "+00002147483645");
                test("\r\u2029+09223372036854775808\t", "  \n0009223372036854775807\u20001\f\f");
                test("+9223372036854775808\t \n0009223372036854775807\n\r\u2000-000009223372036854775808\u2029");
            });
        });
        writer.write("Testing manual tests with all digits...\n");
        writer.scope(() -> {
            test("\f\u0661\u0660  ");
            test("-\u0ED0\u1A80\u06F0\u0BEF8\uABF6\r+\u09ED\u0C6F\u1C59\t\t");
            test(
                    "\u20002\u10914\uA9D7\u1094\u10983\u17E64\uA9F7\u2000 \u200A",
                    "\n\u2004+\u10921\uA8D4\uFF17\u17E483\u0E56\u1B54\u0E56\u1680\u2002");
        });
        writer.write("Testing random tests...\n");
        writer.scope(() -> {
            for (final int size : new int[]{10, 1000, 100000}) {
                writer.write("Testing size " + size + "...\n");
                writer.scope(() -> {
                    writer.write("Testing values [-100..100]...\n");
                    writer.scope(() -> {
                        for (int i = 0; i < 3; i++) {
                            randomTests(size, BigInteger.valueOf(-100), BigInteger.valueOf(101), 4);
                        }
                    });
                    writer.write("Testing values [-2147483648..2147483647]...\n");
                    writer.scope(() -> {
                        for (int i = 0; i < 3; i++) {
                            randomTests(
                                    size,
                                    BigInteger.valueOf(Integer.MIN_VALUE), BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE),
                                    6
                            );
                        }
                    });
                    writer.write("Testing values [0..9223372036854775807]...\n");
                    writer.scope(() -> {
                        for (int i = 0; i < 2; i++) {
                            randomTests(
                                    size,
                                    BigInteger.ZERO, BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE),
                                    10
                            );
                        }
                    });
                    writer.write("Testing values [-9223372036854775808..0]...\n");
                    writer.scope(() -> {
                        for (int i = 0; i < 2; i++) {
                            randomTests(
                                    size,
                                    BigInteger.valueOf(Long.MIN_VALUE), BigInteger.ONE,
                                    10
                            );
                        }
                    });
                    writer.write("Testing random values...\n");
                    writer.scope(() -> {
                        final BigInteger min = new BigInteger("-340282366920938463463374607431768211456");
                        final BigInteger max = min.negate();
                        for (int i = 0; i < 30; i++) {
                            randomTests(size, min, max, 45);
                        }
                    });
                });
            }
        });
    }

    public static void main(final String[] args) throws Exception {
        new SumBigIntegerTest().run();
    }
}

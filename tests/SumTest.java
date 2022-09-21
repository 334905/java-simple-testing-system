import base.IndentingWriter;
import sum.ModeInteger;
import sum.SumTestBase;

public class SumTest extends SumTestBase<Integer> {
    public SumTest() throws ClassNotFoundException, NoSuchMethodException {
        super("Sum", ModeInteger.instance);
    }
    
    private void run() throws Exception {
        writer.write("Testing statement tests...\n");
        writer.scope(() -> {
            test("1", "2", "3");
            test("1", "2", "-3");
            test("1 2 3");
            test("1 +2", "      3");
            test(" ");
            test("2147483647", "1");
        });
        writer.write("Testing manual tests with 0 to 9 digits...\n");
        writer.scope(() -> {
            test("\f-1\t2\n+3\r");
            test();
            test("", "\t", "\f", "\u000B", "\n ");
            test("-0000000000123440\u2029\f+000006675", "-00098890");
            test("  -02147483648  \t", "\f\r-2147483647\u2000 ", "\n\n\n-2147483646", "-00002147483645");
            test("\f\r2147483647\u2000 ", "\n\n\n+2147483646", "+00002147483645");
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
                        for (int i = 0; i < 10; i++) {
                            randomTests(size, -100, 101, 4);
                        }
                    });
                    writer.write("Testing random values...\n");
                    writer.scope(() -> {
                        for (int i = 0; i < 30; i++) {
                            randomTests(size, Integer.MIN_VALUE, Integer.MAX_VALUE, 45);
                        }
                    });
                });
            }
        });
    }

    public static void main(final String[] args) throws Exception {
        new SumTest().run();
    }
}

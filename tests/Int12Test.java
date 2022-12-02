import int_n.IntNTestBase;
import int_n.IntNTester;

public class Int12Test extends IntNTestBase {
    public static void main(String[] args)
            throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        final IntNTester tester = new IntNTester("Int12");

        expectEqual(tester, (short) 0, (short) 0, "0", "0");
        expectGreater(tester, (short) 1, (short) 0, "1", "0");
        expectLess(tester, (short) 0, (short) 1, "0", "1");
        expectGreater(tester, (short) 123, (short) -124, "123", "-124");
        expectGreater(tester, tester.MAX_VALUE, tester.MIN_VALUE, "MAX_VALUE", "MIN_VALUE");

        expectEqual(tester,
                tester.add(tester.create((short) 1), tester.create((short) 1)),
                tester.create((short) 2), "(1 + 1)", "2");
        expectEqual(tester,
                tester.add(tester.create((short) -1), tester.create((short) -1)),
                tester.create((short) -2), "(-1 + -1)", "-2");
        expectEqual(tester,
                tester.add(tester.create((short) -1), tester.create((short) 1)),
                tester.create((short) 0), "(-1 + 1)", "0");
        expectEqual(tester,
                tester.add(tester.create((short) 0), tester.create((short) 1944)),
                tester.create((short) 1944), "(0 + 1944)", "1944");
        expectEqual(tester,
                tester.add(tester.create((short) 1), tester.create((short) 1)),
                tester.create((short) 2), "(1000 + 1000)", "2000");
        expectEqual(tester,
                tester.add(tester.create((short) -1000), tester.create((short) 785)),
                tester.create((short) -215), "(-1000 + 785)", "-215");
        expectEqual(tester,
                tester.subtract(tester.create((short) -382), tester.create((short) -645)),
                tester.create((short) 263), "(-382 - -645)", "263");
        expectEqual(tester,
                tester.subtract(tester.create((short) 5), tester.create((short) -490)),
                tester.create((short) 495), "(5 - -490)", "495");
        expectEqual(tester,
                tester.multiply(tester.create((short) 1), tester.create((short) 1)),
                tester.create((short) 1), "(1 * 1)", "1");
        expectEqual(tester,
                tester.multiply(tester.create((short) 1), tester.create((short) -1)),
                tester.create((short) -1), "(1 * -1)", "-1");
        expectEqual(tester,
                tester.multiply(tester.create((short) -1), tester.create((short) -1)),
                tester.create((short) 1), "(-1 * -1)", "1");
        expectEqual(tester,
                tester.multiply(tester.create((short) 345), tester.create((short) 0)),
                tester.create((short) 0), "(345 * 0)", "0");
        expectEqual(tester,
                tester.multiply(tester.create((short) 5), tester.create((short) -140)),
                tester.create((short) -700), "(5 * -140)", "-700");
        expectEqual(tester,
                tester.multiply(tester.create((short) -785), tester.create((short) -2)),
                tester.create((short) 1570), "(-785 * -2)", "1570");

        expectEqual(tester, tester.create((short) -2048), tester.MIN_VALUE, "-2048", "MIN_VALUE");
        expectEqual(tester, tester.MAX_VALUE, tester.create((short) 2047), "MAX_VALUE", "2047");
        expectEqual(tester, (short) -2048, (short) 2048, "-2048", "2048");
        expectEqual(tester, (short) 4096, (short) 0, "4096", "0");
        expectEqual(tester,
                tester.subtract(tester.MIN_VALUE, tester.create((short) 1)),
                tester.MAX_VALUE, "(MIN_VALUE - 1)", "MAX_VALUE");
        expectEqual(tester,
                tester.multiply(tester.create((short) -2048), tester.create((short) -1)),
                tester.create((short) -2048), "(-2048 * -1)", "-2048");
        expectEqual(tester,
                tester.multiply(tester.create((short) 1193), tester.create((short) -1802)),
                tester.create((short) 614), "(1193 * -1802)", "614");
    }
}

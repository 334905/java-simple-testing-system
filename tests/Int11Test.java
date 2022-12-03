import int_n.IntNTestBase;
import int_n.IntNTester;

public class Int11Test extends IntNTestBase {
    public static void main(String[] args)
            throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        final IntNTester tester = new IntNTester("Int11");

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
                tester.add(tester.create((short) 0), tester.create((short) 944)),
                tester.create((short) 944), "(0 + 944)", "944");
        expectEqual(tester,
                tester.add(tester.create((short) 500), tester.create((short) 500)),
                tester.create((short) 1000), "(500 + 500)", "1000");
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
                tester.multiply(tester.create((short) -324), tester.create((short) -3)),
                tester.create((short) 972), "(-324 * -3)", "972");

        expectEqual(tester, tester.create((short) -1024), tester.MIN_VALUE, "-1024", "MIN_VALUE");
        expectEqual(tester, tester.MAX_VALUE, tester.create((short) 1023), "MAX_VALUE", "1023");
        expectEqual(tester, (short) -1024, (short) 1024, "-1024", "1024");
        expectEqual(tester, (short) 0, (short) 2048, "0", "2048");
        expectEqual(tester, (short) 4096, (short) 0, "4096", "0");
        expectEqual(tester,
                tester.subtract(tester.MIN_VALUE, tester.create((short) 1)),
                tester.MAX_VALUE, "(MIN_VALUE - 1)", "MAX_VALUE");
        expectEqual(tester,
                tester.multiply(tester.create((short) -1024), tester.create((short) -1)),
                tester.create((short) -1024), "(-1024 * -1)", "-1024");
        expectEqual(tester,
                tester.multiply(tester.create((short) 1193), tester.create((short) -1802)),
                tester.create((short) 614), "(1193 * -1802)", "614");
    }
}

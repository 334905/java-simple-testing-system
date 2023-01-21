import reverse.testers.ReverseTester;

public class ReverseTest2 extends ReverseTest {
    public static void main(String[] args) throws Exception {
        final ReverseTester tester = new ReverseTester(random);
        ReverseTest.runTests(tester);
        writer.write("Testing large tests: size 4000000...\n");
        writer.scope(() -> {
            randomTest(tester, 4000000, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            randomTest(tester, 4000000, 2000, Integer.MIN_VALUE, Integer.MAX_VALUE);
            randomTest(tester, 4000000, 4000000, Integer.MIN_VALUE, Integer.MAX_VALUE);
        });
    }
}

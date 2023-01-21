import reverse.testers.ReverseHexTester;

public class ReverseHexTest extends reverse.BaseTest {
    public static void main(String[] args) throws Exception {
        final ReverseHexTester tester = new ReverseHexTester(random);
        writer.write("Testing manual tests...\n");
        writer.scope(() -> {
            test(tester, new int[][]{
                    {1, 2},
                    {3}
            });
            test(tester, new int[][]{
                    {3},
                    {2, 1}
            });
            test(tester, new int[][]{
                    {1},
                    {},
                    {2, -3}
            });
            test(tester, new int[][]{
                    {1, 2},
                    {3, 4}
            });
            test(tester, new int[0][]);
            test(tester, new int[][]{
                    {}
            });
            test(tester, new int[][]{
                    {},
                    {},
                    {}
            });
            test(tester, new int[][]{
                    {1},
            });
            test(tester, new int[][]{
                    {},
                    {},
                    {1},
                    {},
            });
            test(tester, new int[][]{
                    {},
                    {1, 2, 3},
                    {4, 5},
                    {},
                    {6},
                    {},
                    {}
            });
            test(tester, new int[][]{
                    {},
                    {},
                    {},
                    {1, -2, 3, 4},
                    {},
                    {},
                    {},
                    {-5, -6},
                    {},
                    {},
                    {},
                    {7},
                    {},
                    {},
                    {}
            });
        });
        writer.write("Testing random tests...\n");
        writer.scope(() -> {
            writer.write("Testing size 20...\n");
            writer.scope(() -> {
                randomTest(tester, 20, 10, -100, 100);
                randomTest(tester, 20, 25, -100, 100);
                randomTest(tester, 20, 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
                randomTest(tester, 20, 25, Integer.MIN_VALUE, Integer.MAX_VALUE);
            });
            writer.write("Testing size 1000...\n");
            writer.scope(() -> {
                for (int cnt : new int[]{3, 100, 1000, 10000}) {
                    randomTest(tester, 1000, cnt, Integer.MIN_VALUE, Integer.MAX_VALUE);
                }
            });
            writer.write("Testing size 100000...\n");
            writer.scope(() -> {
                randomTest(tester, 100000, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                randomTest(tester, 100000, 1000, Integer.MIN_VALUE, Integer.MAX_VALUE);
                randomTest(tester, 100000, 100000, Integer.MIN_VALUE, Integer.MAX_VALUE);
            });
            writer.write("Testing size 4000000...\n");
            writer.scope(() -> {
                randomTest(tester, 4000000, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                randomTest(tester, 4000000, 2000, Integer.MIN_VALUE, Integer.MAX_VALUE);
                randomTest(tester, 4000000, 4000000, Integer.MIN_VALUE, Integer.MAX_VALUE);
            });
        });
    }
}

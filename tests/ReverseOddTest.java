import reverse.testers.ReverseOddTester;

public class ReverseOddTest extends ReverseEvenTest {
    public static void main(String[] args) throws Exception {
        testAll(new ReverseOddTester(random));
    }
}

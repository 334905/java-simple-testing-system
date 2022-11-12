import line.LineTester;
import line.Point;

import java.util.Objects;

public class LineTest {
    private static void expectTrue(final boolean cond, final String message) {
        if (!cond)
            throw new AssertionError(message);
    }

    private static void expectFalse(final boolean cond, final String message) {
        expectTrue(!cond, message);
    }

    private static void expectEqual(final Object first, final Object second, final String message) {
        expectTrue(Objects.equals(first, second), message);
    }

    public static void main(String[] args) throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
        final LineTester tester = new LineTester();
        expectEqual(tester.intersect(tester.OX, tester.OY), new Point(0, 0), "OX and OY should intersect at (0;0)");
        expectEqual(tester.intersect(tester.OY, tester.OX), new Point(0, 0), "OY and OX should intersect at (0;0)");
        expectTrue(tester.parallelTo(tester.OX, tester.OX), "OX should be parallel to itself");
        expectTrue(tester.parallelTo(tester.OY, tester.OY), "OY should be parallel to itself");
        expectFalse(tester.parallelTo(tester.OX, tester.OY), "OX should not be parallel to OY");
        expectTrue(tester.equalTo(tester.OX, tester.OX), "OX should be equal to itself");
        expectTrue(tester.equalTo(tester.OY, tester.OY), "OY should be equal to itself");
        expectFalse(tester.equalTo(tester.OX, tester.OY), "OX should not be equal to OX");
        expectTrue(tester.perpendicularTo(tester.OX, tester.OY), "OX should be perpendicular to OY");
        expectTrue(tester.perpendicularTo(tester.OY, tester.OX), "OX should be perpendicular to OY");

        expectTrue(tester.equalTo(tester.OX, tester.createOfTwoPoints(new Point(-123, 0), new Point(321, 0))),
                "OX should be equal to (-123;0)---(321;0).");
        expectTrue(tester.equalTo(tester.OY, tester.createOfTwoPoints(new Point(0, -123), new Point(0, 321))),
                "OY should be equal to (0;-123)---(0;321).");

        final Object xEqualsOne = tester.createOfTwoPoints(new Point(1, 0), new Point(1, 1));
        final Object xEqualsNegOne = tester.createOfTwoPoints(new Point(-1, 0), new Point(-1, 1));
        final Object yEqualsOne = tester.createOfTwoPoints(new Point(0, 1), new Point(1, 1));
        final Object yEqualsNegOne = tester.createOfTwoPoints(new Point(0, -1), new Point(1, -1));

        expectTrue(tester.equalTo(xEqualsOne, tester.createOfTwoPoints(new Point(1, 123), new Point(1, 321))),
                "(1;0)---(1;1) should be equal to (1;123)---(1;321)");
        expectTrue(tester.equalTo(xEqualsNegOne, tester.createOfTwoPoints(new Point(-1, 123), new Point(-1, -321))),
                "(-1;0)---(-1;1) should be equal to (-1;123)---(-1;-321)");
        expectTrue(tester.equalTo(yEqualsOne, tester.createOfTwoPoints(new Point(-123, 1), new Point(-321, 1))),
                "(0;1)---(1;1) should be equal to (-123;1)---(-321;1)");
        expectTrue(tester.equalTo(yEqualsNegOne, tester.createOfTwoPoints(new Point(-123, -1), new Point(321, -1))),
                "(0;-1)---(1;-1) should be equal to (-123;-1)---(321;-1)");
        expectTrue(tester.parallelTo(xEqualsOne, tester.OY), "(1;0)---(1;1) should be parallel to OY");
        expectTrue(tester.parallelTo(tester.OY, xEqualsNegOne), "OY should be parallel to (-1;0)---(-1;1)");
        expectTrue(tester.parallelTo(xEqualsOne, xEqualsNegOne), "(1;0)---(1;1) should be parallel to (-1;0)---(-1;1)");
        expectTrue(tester.parallelTo(yEqualsNegOne, tester.OX), "(0;-1)---(1;-1) should be parallel to OX");
        expectTrue(tester.parallelTo(tester.OX, yEqualsOne), "OX should be parallel to (0;1)---(1;1)");
        expectTrue(tester.parallelTo(yEqualsNegOne, yEqualsOne), "(0;-1)---(1;-1) should be parallel to (0;1)---(1;1)");

        expectTrue(tester.perpendicularTo(tester.OX, xEqualsNegOne),
                "OX should be perpendicular to (-1;0)---(-1;1)");
        expectTrue(tester.perpendicularTo(xEqualsNegOne, yEqualsNegOne),
                "(-1;0)---(-1;1) should be perpendicular to (0;-1)---(1;-1)");
        expectTrue(tester.perpendicularTo(yEqualsNegOne, tester.OY),
                "(0;-1)---(1;-1) should be perpendicular to OY");
        expectTrue(tester.perpendicularTo(tester.OY, yEqualsOne),
                "OY should be perpendicular to (0;1)---(1;1)");
        expectTrue(tester.perpendicularTo(yEqualsOne, xEqualsOne),
                "(0;1)---(1;1) should be perpendicular to (1;0)---(1;1)");
        expectTrue(tester.perpendicularTo(xEqualsOne, tester.OX),
                "(1;0)---(1;1) should be perpendicular to OX");
        expectTrue(tester.perpendicularTo(yEqualsOne, xEqualsNegOne),
                "(0;1)---(1;1) should be perpendicular to (-1;0)---(-1;1)");
        expectTrue(tester.perpendicularTo(xEqualsOne, yEqualsNegOne),
                "(1;0)---(1;1) should be perpendicular to (0;-1)---(1;-1)");

        expectEqual(tester.intersect(xEqualsOne, yEqualsNegOne), new Point(1, -1),
                "(1;0)---(1;1) and (0;-1)---(1;-1) should intersect at (1;-1)");
        expectEqual(tester.intersect(xEqualsNegOne, tester.OX), new Point(-1, 0),
                "(-1;0)---(-1;1) and OX should intersect at (1;0)");
        expectEqual(tester.intersect(tester.OY, yEqualsOne), new Point(0, 1),
                "OY and (0;1)---(1;1) should intersect at (0;1)");
    }
}

import line.LineTester;
import line.Point;

public class LineTest {
    private static void expectTrue(final boolean cond, final String message) {
        if (!cond)
            throw new AssertionError(message);
    }

    private static void expectFalse(final boolean cond, final String message) {
        expectTrue(!cond, message);
    }

    private static void expectNearlyEqual(final Point first, final Point second, final String message) {
        final double EPS = 1e-7;
        expectTrue(Math.abs(first.x - second.x) < EPS && Math.abs(first.y - second.y) < EPS, message);
    }

    public static void main(String[] args)
            throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
        final LineTester tester = new LineTester();

        expectTrue(tester.contains(tester.OX, new Point(1.5, 0)),
                "OX должна проходить через (1.5;0)");
        expectTrue(tester.contains(tester.OY, new Point(0, -.21)),
                "OY должна проходить через (0;-0.21)");
        expectFalse(tester.contains(tester.OX, new Point(1, 1)),
                "OX должна не проходить через (1;1)");
        expectFalse(tester.contains(tester.OY, new Point(-7.2, 1)),
                "OY должна не проходить через (-7.2;1)");
        expectNearlyEqual(tester.intersect(tester.OX, tester.OY), new Point(0, 0),
                "OX и OY должны пересекаться в (0;0)");
        expectNearlyEqual(tester.intersect(tester.OY, tester.OX), new Point(0, 0),
                "OY и OX должны пересекаться в (0;0)");
        expectTrue(tester.parallelTo(tester.OX, tester.OX),
                "OX должна быть параллельна самой себе");
        expectTrue(tester.parallelTo(tester.OY, tester.OY),
                "OY должна быть параллельна самой себе");
        expectFalse(tester.parallelTo(tester.OX, tester.OY),
                "OX should not be parallel to OY");
        expectTrue(tester.equalTo(tester.OX, tester.OX),
                "OX должна быть равна самой себе");
        expectTrue(tester.equalTo(tester.OY, tester.OY),
                "OY должна быть равна самой себе");
        expectFalse(tester.equalTo(tester.OX, tester.OY),
                "OX должна быть не равна OY");
        expectTrue(tester.perpendicularTo(tester.OX, tester.OY),
                "OX должна быть перпендикулярна OY");
        expectTrue(tester.perpendicularTo(tester.OY, tester.OX),
                "OX должна быть перпендикулярна OY");

        expectTrue(tester.equalTo(tester.OX, tester.createOfTwoPoints(new Point(-123, 0), new Point(321, 0))),
                "OX должна быть равна (-123;0)---(321;0).");
        expectTrue(tester.equalTo(tester.OY, tester.createOfTwoPoints(new Point(0, -123), new Point(0, 321))),
                "OY должна быть равна (0;-123)---(0;321).");

        final Object xEqualsOne = tester.createOfTwoPoints(new Point(1, 0), new Point(1, 1));
        final Object xEqualsNegOne = tester.createOfTwoPoints(new Point(-1, 0), new Point(-1, 1));
        final Object yEqualsOne = tester.createOfTwoPoints(new Point(0, 1), new Point(1, 1));
        final Object yEqualsNegOne = tester.createOfTwoPoints(new Point(0, -1), new Point(1, -1));

        expectTrue(tester.equalTo(xEqualsOne, tester.createOfTwoPoints(new Point(1, 123), new Point(1, 321))),
                "(1;0)---(1;1) должна быть равна (1;123)---(1;321)");
        expectTrue(tester.equalTo(xEqualsNegOne, tester.createOfTwoPoints(new Point(-1, 123), new Point(-1, -321))),
                "(-1;0)---(-1;1) должна быть равна (-1;123)---(-1;-321)");
        expectTrue(tester.equalTo(yEqualsOne, tester.createOfTwoPoints(new Point(-123, 1), new Point(-321, 1))),
                "(0;1)---(1;1) должна быть равна (-123;1)---(-321;1)");
        expectTrue(tester.equalTo(yEqualsNegOne, tester.createOfTwoPoints(new Point(-123, -1), new Point(321, -1))),
                "(0;-1)---(1;-1) должна быть равна (-123;-1)---(321;-1)");
        expectTrue(tester.parallelTo(xEqualsOne, tester.OY),
                "(1;0)---(1;1) должна быть параллельна OY");
        expectTrue(tester.parallelTo(tester.OY, xEqualsNegOne),
                "OY должна быть параллельна (-1;0)---(-1;1)");
        expectTrue(tester.parallelTo(xEqualsOne, xEqualsNegOne),
                "(1;0)---(1;1) должна быть параллельна (-1;0)---(-1;1)");
        expectTrue(tester.parallelTo(yEqualsNegOne, tester.OX),
                "(0;-1)---(1;-1) должна быть параллельна OX");
        expectTrue(tester.parallelTo(tester.OX, yEqualsOne),
                "OX должна быть параллельна (0;1)---(1;1)");
        expectTrue(tester.parallelTo(yEqualsNegOne, yEqualsOne),
                "(0;-1)---(1;-1) должна быть параллельна (0;1)---(1;1)");

        expectTrue(tester.perpendicularTo(tester.OX, xEqualsNegOne),
                "OX должна быть перпендикулярна (-1;0)---(-1;1)");
        expectTrue(tester.perpendicularTo(xEqualsNegOne, yEqualsNegOne),
                "(-1;0)---(-1;1) должна быть перпендикулярна (0;-1)---(1;-1)");
        expectTrue(tester.perpendicularTo(yEqualsNegOne, tester.OY),
                "(0;-1)---(1;-1) должна быть перпендикулярна OY");
        expectTrue(tester.perpendicularTo(tester.OY, yEqualsOne),
                "OY должна быть перпендикулярна (0;1)---(1;1)");
        expectTrue(tester.perpendicularTo(yEqualsOne, xEqualsOne),
                "(0;1)---(1;1) должна быть перпендикулярна (1;0)---(1;1)");
        expectTrue(tester.perpendicularTo(xEqualsOne, tester.OX),
                "(1;0)---(1;1) должна быть перпендикулярна OX");
        expectTrue(tester.perpendicularTo(yEqualsOne, xEqualsNegOne),
                "(0;1)---(1;1) должна быть перпендикулярна (-1;0)---(-1;1)");
        expectTrue(tester.perpendicularTo(xEqualsOne, yEqualsNegOne),
                "(1;0)---(1;1) должна быть перпендикулярна (0;-1)---(1;-1)");

        expectNearlyEqual(tester.intersect(xEqualsOne, yEqualsNegOne), new Point(1, -1),
                "(1;0)---(1;1) и (0;-1)---(1;-1) должны пересекаться в (1;-1)");
        expectNearlyEqual(tester.intersect(xEqualsNegOne, tester.OX), new Point(-1, 0),
                "(-1;0)---(-1;1) и OX должны пересекаться в (1;0)");
        expectNearlyEqual(tester.intersect(tester.OY, yEqualsOne), new Point(0, 1),
                "OY и (0;1)---(1;1) должны пересекаться в (0;1)");

        expectTrue(tester.contains(xEqualsOne, new Point(1, -2.5)),
                "(1;0)---(1;1) должна проходить через (1;-2.5)");
        expectTrue(tester.contains(xEqualsNegOne, new Point(-1, .25)),
                "(-1;0)---(-1;1) должна проходить через (-1;0.25)");
        expectTrue(tester.contains(yEqualsOne, new Point(0, 1)),
                "(0;1)---(1;1) должна проходить через (0;1)");

        final Object line1 = tester.createOfTwoPoints(new Point(0, 1), new Point(2, 4)); //1.5x+1
        final Object line2 = tester.createOfTwoPoints(new Point(0, -3), new Point(-3, -1)); //-2/3x-3
        expectTrue(tester.contains(line2, new Point(-4.5, 0)),
                "(0;-3)---(-3;-1) должна проходить через (-4.5;0)");
        expectTrue(tester.contains(line1, new Point(-2, -2)),
                "(0;1)---(2;4) должна проходить через (-2;-2)");
        expectTrue(tester.perpendicularTo(line1, line2),
                "(0;1)---(2;4) должна быть перпендикулярна (0;-3)---(-3;-1)");
        expectTrue(tester.perpendicularTo(line2, line1),
                "(0;-3)---(-3;-1) должна быть перпендикулярна (0;1)---(2;4)");
        expectTrue(tester.parallelTo(line1, tester.createOfTwoPoints(new Point(0, 0), new Point(2, 3))),
                "(0;1)---(2;4) должна быть параллельна (0;0)---(2;3)");
        expectFalse(tester.equalTo(line1, tester.createOfTwoPoints(new Point(0, 0), new Point(2, 3))),
                "(0;1)---(2;4) должна быть не равна (0;0)---(2;3)");
        expectNearlyEqual(tester.intersect(line1, line2), new Point(-24.0/13, -23.0/13),
                "(0;1)---(2;4) и (0;-3)---(-3;-1) должны пересекаться в (-24/13;-23/13)");
    }
}

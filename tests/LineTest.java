import line.LineTester;
import line.Point;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

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
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (1.5;0)");
        expectTrue(tester.contains(tester.OY, new Point(0, -.21)),
                "OY \u0434\u043e\u043b\u0436\u043d\u0430 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (0;-0.21)");
        expectFalse(tester.contains(tester.OX, new Point(1, 1)),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u043d\u0435 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (1;1)");
        expectFalse(tester.contains(tester.OY, new Point(-7.2, 1)),
                "OY \u0434\u043e\u043b\u0436\u043d\u0430 \u043d\u0435 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (-7.2;1)");
        expectNearlyEqual(tester.intersect(tester.OX, tester.OY), new Point(0, 0),
                "OX \u0438 OY \u0434\u043e\u043b\u0436\u043d\u044b \u043f\u0435\u0440\u0435\u0441\u0435\u043a\u0430\u0442\u044c\u0441\u044f \u0432 (0;0)");
        expectNearlyEqual(tester.intersect(tester.OY, tester.OX), new Point(0, 0),
                "OY \u0438 OX \u0434\u043e\u043b\u0436\u043d\u044b \u043f\u0435\u0440\u0435\u0441\u0435\u043a\u0430\u0442\u044c\u0441\u044f \u0432 (0;0)");
        expectTrue(tester.parallelTo(tester.OX, tester.OX),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 \u0441\u0430\u043c\u043e\u0439 \u0441\u0435\u0431\u0435");
        expectTrue(tester.parallelTo(tester.OY, tester.OY),
                "OY \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 \u0441\u0430\u043c\u043e\u0439 \u0441\u0435\u0431\u0435");
        expectFalse(tester.parallelTo(tester.OX, tester.OY),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043d\u0435 \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 OY");
        expectTrue(tester.equalTo(tester.OX, tester.OX),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0440\u0430\u0432\u043d\u0430 \u0441\u0430\u043c\u043e\u0439 \u0441\u0435\u0431\u0435");
        expectTrue(tester.equalTo(tester.OY, tester.OY),
                "OY \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0440\u0430\u0432\u043d\u0430 \u0441\u0430\u043c\u043e\u0439 \u0441\u0435\u0431\u0435");
        expectFalse(tester.equalTo(tester.OX, tester.OY),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043d\u0435 \u0440\u0430\u0432\u043d\u0430 OY");
        expectTrue(tester.perpendicularTo(tester.OX, tester.OY),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 OY");
        expectTrue(tester.perpendicularTo(tester.OY, tester.OX),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 OY");

        expectTrue(tester.equalTo(tester.OX, tester.createOfTwoPoints(new Point(-123, 0), new Point(321, 0))),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0440\u0430\u0432\u043d\u0430 (-123;0)---(321;0).");
        expectTrue(tester.equalTo(tester.OY, tester.createOfTwoPoints(new Point(0, -123), new Point(0, 321))),
                "OY \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0440\u0430\u0432\u043d\u0430 (0;-123)---(0;321).");

        final Object xEqualsOne = tester.createOfTwoPoints(new Point(1, 0), new Point(1, 1));
        final Object xEqualsNegOne = tester.createOfTwoPoints(new Point(-1, 0), new Point(-1, 1));
        final Object yEqualsOne = tester.createOfTwoPoints(new Point(0, 1), new Point(1, 1));
        final Object yEqualsNegOne = tester.createOfTwoPoints(new Point(0, -1), new Point(1, -1));

        expectTrue(tester.equalTo(xEqualsOne, tester.createOfTwoPoints(new Point(1, 123), new Point(1, 321))),
                "(1;0)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0440\u0430\u0432\u043d\u0430 (1;123)---(1;321)");
        expectTrue(tester.equalTo(xEqualsNegOne, tester.createOfTwoPoints(new Point(-1, 123), new Point(-1, -321))),
                "(-1;0)---(-1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0440\u0430\u0432\u043d\u0430 (-1;123)---(-1;-321)");
        expectTrue(tester.equalTo(yEqualsOne, tester.createOfTwoPoints(new Point(-123, 1), new Point(-321, 1))),
                "(0;1)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0440\u0430\u0432\u043d\u0430 (-123;1)---(-321;1)");
        expectTrue(tester.equalTo(yEqualsNegOne, tester.createOfTwoPoints(new Point(-123, -1), new Point(321, -1))),
                "(0;-1)---(1;-1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u0440\u0430\u0432\u043d\u0430 (-123;-1)---(321;-1)");
        expectTrue(tester.parallelTo(xEqualsOne, tester.OY),
                "(1;0)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 OY");
        expectTrue(tester.parallelTo(tester.OY, xEqualsNegOne),
                "OY \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 (-1;0)---(-1;1)");
        expectTrue(tester.parallelTo(xEqualsOne, xEqualsNegOne),
                "(1;0)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 (-1;0)---(-1;1)");
        expectTrue(tester.parallelTo(yEqualsNegOne, tester.OX),
                "(0;-1)---(1;-1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 OX");
        expectTrue(tester.parallelTo(tester.OX, yEqualsOne),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 (0;1)---(1;1)");
        expectTrue(tester.parallelTo(yEqualsNegOne, yEqualsOne),
                "(0;-1)---(1;-1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 (0;1)---(1;1)");

        expectTrue(tester.perpendicularTo(tester.OX, xEqualsNegOne),
                "OX \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 (-1;0)---(-1;1)");
        expectTrue(tester.perpendicularTo(xEqualsNegOne, yEqualsNegOne),
                "(-1;0)---(-1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 (0;-1)---(1;-1)");
        expectTrue(tester.perpendicularTo(yEqualsNegOne, tester.OY),
                "(0;-1)---(1;-1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 OY");
        expectTrue(tester.perpendicularTo(tester.OY, yEqualsOne),
                "OY \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 (0;1)---(1;1)");
        expectTrue(tester.perpendicularTo(yEqualsOne, xEqualsOne),
                "(0;1)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 (1;0)---(1;1)");
        expectTrue(tester.perpendicularTo(xEqualsOne, tester.OX),
                "(1;0)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 OX");
        expectTrue(tester.perpendicularTo(yEqualsOne, xEqualsNegOne),
                "(0;1)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 (-1;0)---(-1;1)");
        expectTrue(tester.perpendicularTo(xEqualsOne, yEqualsNegOne),
                "(1;0)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 (0;-1)---(1;-1)");

        expectNearlyEqual(tester.intersect(xEqualsOne, yEqualsNegOne), new Point(1, -1),
                "(1;0)---(1;1) \u0438 (0;-1)---(1;-1) \u0434\u043e\u043b\u0436\u043d\u044b \u043f\u0435\u0440\u0435\u0441\u0435\u043a\u0430\u0442\u044c\u0441\u044f \u0432 (1;-1)");
        expectNearlyEqual(tester.intersect(xEqualsNegOne, tester.OX), new Point(-1, 0),
                "(-1;0)---(-1;1) \u0438 OX \u0434\u043e\u043b\u0436\u043d\u044b \u043f\u0435\u0440\u0435\u0441\u0435\u043a\u0430\u0442\u044c\u0441\u044f \u0432 (1;0)");
        expectNearlyEqual(tester.intersect(tester.OY, yEqualsOne), new Point(0, 1),
                "OY \u0438 (0;1)---(1;1) \u0434\u043e\u043b\u0436\u043d\u044b \u043f\u0435\u0440\u0435\u0441\u0435\u043a\u0430\u0442\u044c\u0441\u044f \u0432 (0;1)");

        expectTrue(tester.contains(xEqualsOne, new Point(1, -2.5)),
                "(1;0)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (1;-2.5)");
        expectTrue(tester.contains(xEqualsNegOne, new Point(-1, .25)),
                "(-1;0)---(-1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (-1;0.25)");
        expectTrue(tester.contains(yEqualsOne, new Point(0, 1)),
                "(0;1)---(1;1) \u0434\u043e\u043b\u0436\u043d\u0430 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (0;1)");

        final Object line1 = tester.createOfTwoPoints(new Point(0, 1), new Point(2, 4)); //1.5x+1
        final Object line2 = tester.createOfTwoPoints(new Point(0, -3), new Point(-3, -1)); //-2/3x-3
        expectTrue(tester.contains(line2, new Point(-4.5, 0)),
                "(0;-3)---(-3;-1) \u0434\u043e\u043b\u0436\u043d\u0430 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (-4.5;0)");
        expectTrue(tester.contains(line1, new Point(-2, -2)),
                "(0;1)---(2;4) \u0434\u043e\u043b\u0436\u043d\u0430 \u043f\u0440\u043e\u0445\u043e\u0434\u0438\u0442\u044c \u0447\u0435\u0440\u0435\u0437 (-2;-2)");
        expectTrue(tester.perpendicularTo(line1, line2),
                "(0;1)---(2;4) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 (0;-3)---(-3;-1)");
        expectTrue(tester.perpendicularTo(line2, line1),
                "(0;-3)---(-3;-1) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0435\u0440\u043f\u0435\u043d\u0434\u0438\u043a\u0443\u043b\u044f\u0440\u043d\u0430 (0;1)---(2;4)");
        expectTrue(tester.parallelTo(line1, tester.createOfTwoPoints(new Point(0, 0), new Point(2, 3))),
                "(0;1)---(2;4) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043f\u0430\u0440\u0430\u043b\u043b\u0435\u043b\u044c\u043d\u0430 (0;0)---(2;3)");
        expectFalse(tester.equalTo(line1, tester.createOfTwoPoints(new Point(0, 0), new Point(2, 3))),
                "(0;1)---(2;4) \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043d\u0435 \u0440\u0430\u0432\u043d\u0430 (0;0)---(2;3)");
        expectNearlyEqual(tester.intersect(line1, line2), new Point(-24.0/13, -23.0/13),
                "(0;1)---(2;4) \u0438 (0;-3)---(-3;-1) \u0434\u043e\u043b\u0436\u043d\u044b \u043f\u0435\u0440\u0435\u0441\u0435\u043a\u0430\u0442\u044c\u0441\u044f \u0432 (-24/13;-23/13)");
    }
}

package line;

import base.testers.ClassTester;

import java.lang.reflect.Method;

public class LineTester extends ClassTester {
    private final Method ofTwoPoints;

    private final Method contains;
    private final Method intersect;
    private final Method parallelTo;
    private final Method equalTo;
    private final Method perpendicularTo;

    public final Object OX;
    public final Object OY;

    public LineTester() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        super("Line");

        ofTwoPoints = aClass.getMethod("ofTwoPoints", Point.class, Point.class);

        contains = aClass.getMethod("contains", Point.class);
        intersect = aClass.getMethod("intersect", aClass);
        parallelTo = aClass.getMethod("parallelTo", aClass);
        equalTo = aClass.getMethod("equalTo", aClass);
        perpendicularTo = aClass.getMethod("perpendicularTo", aClass);

        OX = aClass.getField("OX").get(null);
        OY = aClass.getField("OY").get(null);
    }

    public Object createOfTwoPoints(Point point1, Point point2) throws IllegalAccessException {
        return runMethod(null, ofTwoPoints, point1, point2).getValue();
    }

    public boolean contains(Object line, Point point) throws IllegalAccessException {
        return this.<Boolean>runMethod(line, contains, point).getValue();
    }
    public Point intersect(Object line, Object other) throws IllegalAccessException {
        return this.<Point>runMethod(line, intersect, other).getValue();
    }
    public boolean parallelTo(Object line, Object other) throws IllegalAccessException {
        return this.<Boolean>runMethod(line, parallelTo, other).getValue();
    }
    public boolean equalTo(Object line, Object other) throws IllegalAccessException {
        return this.<Boolean>runMethod(line, equalTo, other).getValue();
    }
    public boolean perpendicularTo(Object line, Object other) throws IllegalAccessException {
        return this.<Boolean>runMethod(line, perpendicularTo, other).getValue();
    }
}

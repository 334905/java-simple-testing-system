package int_n;

import base.testers.ClassTester;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Int12Tester extends ClassTester {
    private final Method add;
    private final Method subtract;
    private final Method multiply;
    private final Method compareTo;

    private final Constructor<?> ofShort;

    public final Object MAX_VALUE;
    public final Object MIN_VALUE;

    public Int12Tester()
            throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        super("Int12");

        add = aClass.getMethod("add", aClass);
        subtract = aClass.getMethod("subtract", aClass);
        multiply = aClass.getMethod("multiply", aClass);
        compareTo = aClass.getMethod("compareTo", aClass);

        MAX_VALUE = aClass.getField("MAX_VALUE").get(null);
        MIN_VALUE = aClass.getField("MIN_VALUE").get(null);

        ofShort = aClass.getConstructor(short.class);
    }

    public Object create(short value) throws InstantiationException, IllegalAccessException {
        return runConstructor(ofShort, value).getValue();
    }
    public Object add(Object first, Object second) throws IllegalAccessException {
        return runMethod(first, add, second).getValue();
    }
    public Object subtract(Object first, Object second) throws IllegalAccessException {
        return runMethod(first, subtract, second).getValue();
    }
    public Object multiply(Object first, Object second) throws IllegalAccessException {
        return runMethod(first, multiply, second).getValue();
    }
    public int compare(Object first, Object second) throws IllegalAccessException {
        return this.<Integer>runMethod(first, compareTo, second).getValue();
    }
}

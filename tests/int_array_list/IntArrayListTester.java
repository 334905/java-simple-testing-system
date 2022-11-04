package int_array_list;

import base.expected.Expected;
import base.testers.ClassTester;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class IntArrayListTester extends ClassTester {
    private final Constructor<?> aDefault; // IntArrayList()
    private final Method ofArray;          // IntArrayList IntArrayList.of(int[])

    private final Method addEnd;           // void this.add(int)
    private final Method removeEnd;        // void this.remove
    private final Method get;              // void this.get(int)
    private final Method set;              // void this.set(int, int)

    private final Method size;             // int this.size()
    private final Method isEmpty;          // boolean this.isEmpty()

    /*
    private final Method capacity;         // int this.capacity()
    private final Method ensureCapacity;   // void this.ensureCapacity(int)
    private final Method trimToSize;       // void this.trimToSize()
     */

    private final Method toString;         // String this.toString()
    private final Method toArray;          // int[] this.toArray()

    public IntArrayListTester() throws ClassNotFoundException, NoSuchMethodException {
        super("IntArrayList");

        aDefault = aClass.getConstructor();
        ofArray = aClass.getMethod("of", int[].class);

        addEnd = aClass.getMethod("add", int.class);
        removeEnd = aClass.getMethod("remove");
        get = aClass.getMethod("get", int.class);
        set = aClass.getMethod("set", int.class, int.class);

        size = aClass.getMethod("size");
        isEmpty = aClass.getMethod("isEmpty");

        toString = aClass.getMethod("toString");
        toArray = aClass.getMethod("toArray");
    }

    public Object newIntArrayList() throws InstantiationException, IllegalAccessException {
        return runConstructor(aDefault).getValue();
    }

    public Object ofArray(final int[] array) throws IllegalAccessException {
        return runMethod(null, ofArray, (Object) array).getValue();
    }

    public void add(final Object self, final int elem) throws IllegalAccessException {
        runMethod(self, addEnd, elem).getValue();
    }

    public Expected<Void, Exception> remove(final Object self) throws IllegalAccessException {
        return runMethod(self, removeEnd);
    }

    public Expected<Integer, Exception> get(final Object self, final int index) throws IllegalAccessException {
        return runMethod(self, get, index);
    }

    public Expected<Void, Exception> set(final Object self, final int index, final int elem) throws IllegalAccessException {
        return runMethod(self, set, index, elem);
    }

    public int size(final Object self) throws IllegalAccessException {
        return this.<Integer>runMethod(self, size).getValue();
    }

    public boolean isEmpty(final Object self) throws IllegalAccessException {
        return this.<Boolean>runMethod(self, isEmpty).getValue();
    }

    public String toString(final Object self) throws IllegalAccessException {
        return this.<String>runMethod(self, toString).getValue();
    }

    public int[] toArray(final Object self) throws IllegalAccessException {
        return this.<int[]>runMethod(self, toArray).getValue();
    }
}

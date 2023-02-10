package mutable_vector_array_list;

import base.expected.Expected;
import base.testers.ClassTester;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MutableVectorArrayListTester extends ClassTester {
    private final Method add;
    // private final Method addIndexed;
    private final Method capacity;
    // private final Method clear;
    // private final Method contains;
    // private final Method ensureCapacity;
    // private final Method equals;
    private final Method get;
    // private final Method indexOf;
    private final Method isEmpty;
    // private final Method lastIndexOf;
    private final Method removeIndexed;
    // private final Method remove;
    private final Method set;
    private final Method size;
    // private final Method toArray;
    // private final Method trimToSize;
    private final Constructor<?> defaultConstructor;
    private final Constructor<?> constructorOfCapacity;
    // private final Constructor<?> constructorOfArray;

    public MutableVectorArrayListTester() throws ClassNotFoundException, NoSuchMethodException {
        super("MutableVectorArrayList");

        add = getMethod(boolean.class, "add", MutableVector.class);
        capacity = getMethod(int.class, "capacity");
        get = getMethod(MutableVector.class, "get", int.class);
        isEmpty = getMethod(boolean.class, "isEmpty");
        removeIndexed = getMethod(MutableVector.class, "remove", int.class);
        set = getMethod(void.class, "set", int.class, MutableVector.class);
        size = getMethod(int.class, "size");

        defaultConstructor = aClass.getConstructor();
        constructorOfCapacity = aClass.getConstructor(int.class);
    }

    public boolean add(final Object list, final MutableVector elem) throws IllegalAccessException {
        return super.<Boolean>runMethod(list, add, elem).getValue();
    }

    public int capacity(final Object list) throws IllegalAccessException {
        return super.<Integer>runMethod(list, capacity).getValue();
    }

    public Expected<MutableVector, Exception> get(final Object list, final int index) throws IllegalAccessException {
        return super.runMethod(list, get, index);
    }

    public boolean isEmpty(final Object list) throws IllegalAccessException {
        return super.<Boolean>runMethod(list, isEmpty).getValue();
    }

    public Expected<MutableVector, Exception> remove(final Object list, final int index) throws IllegalAccessException {
        return super.runMethod(list, removeIndexed, index);
    }

    public Expected<Void, Exception> set(final Object list, final int index, final MutableVector elem)
            throws IllegalAccessException {
        return super.runMethod(list, set, index, elem);
    }

    public int size(final Object list) throws IllegalAccessException {
        return super.<Integer>runMethod(list, size).getValue();
    }

    public Object newList() throws IllegalAccessException, InstantiationException {
        return super.runConstructor(defaultConstructor).getValue();
    }

    public Object newList(final int capacity) throws IllegalAccessException, InstantiationException {
        return super.runConstructor(constructorOfCapacity, capacity).getValue();
    }
}

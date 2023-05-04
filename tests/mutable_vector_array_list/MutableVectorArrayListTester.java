package mutable_vector_array_list;

import base.either.expected.Expected;
import base.testers.ClassTester;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MutableVectorArrayListTester extends ClassTester {
    private final Method add;
    // private final Method addIndexed; REMOVED
    private final Method capacity;
    private final Method clear;
    private final Method contains;
    private final Method ensureCapacity;
    private final Method equals;
    private final Method get;
    private final Method indexOf;
    private final Method isEmpty;
    // private final Method lastIndexOf; REMOVED
    private final Method removeIndexed;
    private final Method remove;
    private final Method set;
    private final Method size;
    private final Method toArray;
    private final Method trimToSize;
    private final Constructor<?> defaultConstructor;
    private final Constructor<?> constructorOfCapacity;
    private final Constructor<?> constructorOfArray;

    public MutableVectorArrayListTester() throws ClassNotFoundException, NoSuchMethodException {
        super("MutableVectorArrayList");

        add = getMethod(boolean.class, "add", MutableVector.class);
        capacity = getMethod(int.class, "capacity");
        get = getMethod(MutableVector.class, "get", int.class);
        isEmpty = getMethod(boolean.class, "isEmpty");
        removeIndexed = getMethod(MutableVector.class, "remove", int.class);
        set = getMethod(void.class, "set", int.class, MutableVector.class);
        size = getMethod(int.class, "size");

        indexOf = getMethod(int.class, "indexOf", MutableVector.class);
        contains = getMethod(boolean.class, "contains", MutableVector.class);
        remove = getMethod(boolean.class, "remove", MutableVector.class);

        clear = getMethod(void.class, "clear");

        equals = getMethod(boolean.class, "equals", Object.class);

        ensureCapacity = getMethod(void.class, "ensureCapacity", int.class);
        trimToSize = getMethod(void.class, "trimToSize");

        toArray = getMethod(MutableVector[].class, "toArray");

        defaultConstructor = aClass.getConstructor();
        constructorOfCapacity = aClass.getConstructor(int.class);
        constructorOfArray = aClass.getConstructor(MutableVector[].class);
    }

    public boolean add(final Object list, final MutableVector elem) throws IllegalAccessException {
        return super.<Boolean>runMethod(list, add, elem).getValue();
    }

    public int capacity(final Object list) throws IllegalAccessException {
        return super.<Integer>runMethod(list, capacity).getValue();
    }

    public Expected<MutableVector, Exception> expectedGet(final Object list, final int index) throws IllegalAccessException {
        return super.runMethod(list, get, index);
    }

    public boolean isEmpty(final Object list) throws IllegalAccessException {
        return super.<Boolean>runMethod(list, isEmpty).getValue();
    }

    public Expected<MutableVector, Exception> expectedRemove(final Object list, final int index) throws IllegalAccessException {
        return super.runMethod(list, removeIndexed, index);
    }

    public Expected<Void, Exception> expectedSet(final Object list, final int index, final MutableVector elem)
            throws IllegalAccessException {
        return super.runMethod(list, set, index, elem);
    }

    public int size(final Object list) throws IllegalAccessException {
        return super.<Integer>runMethod(list, size).getValue();
    }

    public int indexOf(final Object list, final MutableVector vector) throws IllegalAccessException {
        return super.<Integer>runMethod(list, indexOf, vector).getValue();
    }

    public boolean contains(final Object list, final MutableVector vector) throws IllegalAccessException {
        return super.<Boolean>runMethod(list, contains, vector).getValue();
    }

    public boolean remove(final Object list, final MutableVector vector) throws IllegalAccessException {
        return super.<Boolean>runMethod(list, remove, vector).getValue();
    }

    public void clear(final Object list) throws IllegalAccessException {
        super.<Void>runMethod(list, clear).getValue();
    }

    public boolean equals(final Object list, final Object other) throws IllegalAccessException {
        return super.<Boolean>runMethod(list, equals, other).getValue();
    }

    public Expected<Void, Exception> expectedEnsureCapacity(final Object list, final int newCapacity) throws IllegalAccessException {
        return super.runMethod(list, ensureCapacity, newCapacity);
    }

    public void trimToSize(final Object list) throws IllegalAccessException {
        super.<Void>runMethod(list, trimToSize).getValue();
    }

    public MutableVector[] toArray(final Object list) throws IllegalAccessException {
        return super.<MutableVector[]>runMethod(list, toArray).getValue();
    }

    public Expected<?, Exception> expectedNewList() throws IllegalAccessException, InstantiationException {
        return super.runConstructor(defaultConstructor);
    }

    public Object newList() throws IllegalAccessException, InstantiationException {
        return expectedNewList().getValue();
    }

    public Expected<?, Exception> expectedNewList(final int capacity) throws IllegalAccessException, InstantiationException {
        return super.runConstructor(constructorOfCapacity, capacity);
    }

    public Object newList(final int capacity) throws IllegalAccessException, InstantiationException {
        return expectedNewList(capacity).getValue();
    }

    public Object newList(final MutableVector[] array) throws IllegalAccessException, InstantiationException {
        return super.runConstructor(constructorOfArray, (Object) array).getValue();
    }
}

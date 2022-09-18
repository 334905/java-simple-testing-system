package base;

import java.util.Arrays;

public class IntArrayList {
    private int[] data = new int[0];
    private int size = 0;

    public IntArrayList(int[] data) throws NullPointerException {
        this.data = Arrays.copyOf(data, data.length);
        size = data.length;
    }

    public IntArrayList() {
    }

    public void add(int elem) {
        if (size == data.length) {
            data = Arrays.copyOf(data, size * 2 + 1);
        }
        data[size++] = elem;
    }

    public int get(int index) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + size);
        } else {
            return data[index];
        }
    }

    public void set(int index, int value) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + size);
        } else {
            data[index] = value;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void trimToSize() {
        data = Arrays.copyOf(data, size);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            builder.append(data[i]);
            builder.append(' ');
        }
        if (size != 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(']');
        return builder.toString();
    }

    public int[] toArray() {
        if (size == data.length) {
            return data;
        } else {
            return Arrays.copyOf(data, size);
        }
    }
}

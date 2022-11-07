package reverse;

import base.ExtendedRandom;
import base.testers.MainTester;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractReverseTester extends MainTester<Void, int[][], int[][]> {
    public AbstractReverseTester(final String className, final ExtendedRandom random) throws ClassNotFoundException, NoSuchMethodException {
        super(className);
        this.random = random;
    }

    final ExtendedRandom random;

    public static class IntArrayList {
        private int[] data = new int[0];
        private int size = 0;

        public static IntArrayList of(int[] data) throws NullPointerException {
            IntArrayList list = new IntArrayList();
            list.data = Arrays.copyOf(data, data.length);
            list.size = data.length;
            return list;
        }

        public IntArrayList() {
        }

        public void add(int elem) {
            if (size == data.length) {
                data = Arrays.copyOf(data, size * 2 + 1);
            }
            data[size++] = elem;
        }

        public void remove() {
            size--;
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
            return Arrays.copyOf(data, size);
        }
    }

    @Override
    protected List<String> convertInput(final int[][] input) {
        Supplier<String> spaces = () -> random.nextStringFrom(1, 10, " ");
        return Arrays.stream(input).map(
                a -> Arrays.stream(a)
                        .mapToObj(Integer::toString)
                        .reduce((s1, s2) -> s1 + spaces.get() + s2)
                        .map(s -> spaces.get() + s + spaces.get())
                        .orElseGet(spaces)
        ).toList();
    }

    @Override
    protected String[] convertArgs(final Void args) {
        return new String[0];
    }

    @Override
    protected int[][] convertOutput(final List<String> output) {
        if (output.isEmpty()) {
            return new int[0][];
        }
        return output.stream().map(
                s -> {
                    s = s.strip();
                    if (s.isEmpty()) {
                        return new int[0];
                    } else {
                        return Arrays.stream(s.split(" "))
                                .mapToInt(Integer::parseInt)
                                .toArray();
                    }
                }
        ).toArray(int[][]::new);
    }
}

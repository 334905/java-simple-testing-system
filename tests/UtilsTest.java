import base.IndentingWriter;
import base.expected.Expected;
import base.testers.ClassTester;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UtilsTest {
    private static class UtilsTester extends ClassTester {
        private final Method equals;
        private final Method toString;
        private final Method toStringWithDefault;
        private final Method requireNonNull;
        private final Method requireNonNullElse;
        private final Method fill;
        private final Method toStringArray;
        private final Method mismatch;

        public UtilsTester() throws ClassNotFoundException, NoSuchMethodException {
            super("Utils");

            equals = aClass.getMethod("equals", Object.class, Object.class);
            toString = aClass.getMethod("toString", Object.class);
            toStringArray = aClass.getMethod("toString", Object[].class);
            toStringWithDefault = aClass.getMethod("toString", Object.class, String.class);
            requireNonNull = aClass.getMethod("requireNonNull", Object.class);
            requireNonNullElse = aClass.getMethod("requireNonNullElse", Object.class, Object.class);
            mismatch = aClass.getMethod("mismatch", Object[].class, Object[].class);
            fill = aClass.getMethod("fill", Object[].class, Object.class);

            Constructor<?>[] constructors = aClass.getConstructors();
            if (constructors.length != 0) {
                throw new AssertionError("Expected Utils to have no constructors, but the following ones found: "
                        + Arrays.toString(constructors));
            }
        }

        public boolean equals(Object a, Object b) throws IllegalAccessException {
            return this.<Boolean>runMethod(null, equals, a, b).getValue();
        }

        public String toString(Object obj) throws IllegalAccessException {
            return this.<String>runMethod(null, toString, obj).getValue();
        }

        public String toString(Object obj, String nullDefault) throws IllegalAccessException {
            return this.<String>runMethod(null, toStringWithDefault, obj, nullDefault).getValue();
        }

        public Expected<Object, NullPointerException> requireNonNull(Object obj) throws IllegalAccessException {
            final Expected<Object, Exception> expected = runMethod(null, requireNonNull, obj);
            if (expected.hasValue()) {
                return Expected.ofValue(expected.getValue());
            } else if (expected.getError() instanceof NullPointerException exception) {
                return Expected.ofError(exception);
            } else {
                throw new AssertionError(
                        "Exception " + expected.getError().getClass() + " thrown from requireNonNull",
                        expected.getError()
                );
            }
        }

        public Expected<Object, NullPointerException> requireNonNullElse(Object obj, Object defaultObj) throws IllegalAccessException {
            final Expected<Object, Exception> expected = runMethod(null, requireNonNullElse, obj, defaultObj);
            if (expected.hasValue()) {
                return Expected.ofValue(expected.getValue());
            } else if (expected.getError() instanceof NullPointerException exception) {
                return Expected.ofError(exception);
            } else {
                throw new AssertionError(
                        "Exception " + expected.getError().getClass() + " thrown from requireNonNullElse",
                        expected.getError()
                );
            }
        }

        public void fill(Object[] a, Object val) throws IllegalAccessException {
            this.<Void>runMethod(null, fill, a, val).getValue();
        }

        public String toString(Object[] a) throws IllegalAccessException {
            return this.<String>runMethod(null, toStringArray, (Object) a).getValue();
        }

        public int mismatch(Object[] a, Object[] b) throws IllegalAccessException {
            return this.<Integer>runMethod(null, mismatch, a, b).getValue();
        }
    }

    public static void main(String[] args) throws Exception {
        final UtilsTester tester = new UtilsTester();
        final IndentingWriter writer = new IndentingWriter(
                new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));

        writer.write("Testing requireNonNull:\n");
        writer.scope(() -> {
            if (tester.requireNonNull(null).hasValue())
                throw new AssertionError("requireNonNull(null) is expected to throw NullPointerException.");
            if (tester.requireNonNull("abc")
                    .getValueOrThrow(new AssertionError("requireNonNull(\"abc\") is expected not to throw.")) != "abc") {
                throw new AssertionError("requireNonNull(\"abc\") is expected to be equal to \"abc\".");
            }
            final Object obj = new Object();
            if (tester.requireNonNull(obj)
                    .getValueOrThrow(new AssertionError("requireNonNull(new Object()) is expected not to throw.")) != obj) {
                throw new AssertionError("requireNonNull(new Object()) is expected to be equal to the same object.");
            }
            final int[] arr = new int[0];
            if (tester.requireNonNull(arr)
                    .getValueOrThrow(new AssertionError("requireNonNull(new int[0]) is expected not to throw.")) != arr) {
                throw new AssertionError("requireNonNull(new int[0]) is expected to be equal to the same array.");
            }
        });
        writer.write("Testing requireNonNullElse:\n");
        writer.scope(() -> {
            if (tester.requireNonNullElse("abc", null)
                    .getValueOrThrow(new AssertionError("requireNonNullElse(\"abc\", null) is expected not to throw.")) != "abc") {
                throw new AssertionError("requireNonNullElse(\"abc\", null) is expected to be equal to \"abc\".");
            }
            Object obj = new Object();
            int[] arr = new int[0];
            if (tester.requireNonNullElse(obj, arr)
                    .getValueOrThrow(new AssertionError("requireNonNullElse(new Object(), new int[0]) is expected not to throw.")) != obj) {
                throw new AssertionError("requireNonNullElse(new Object(), new int[0]) is expected to be equal to the object.");
            }
            if (tester.requireNonNullElse(null, arr)
                    .getValueOrThrow(new AssertionError("requireNonNull(null, new int[0]) is expected not to throw.")) != arr) {
                throw new AssertionError("requireNonNull(null, new int[0]) is expected to be equal to the array.");
            }
            if (tester.requireNonNullElse(null, null).hasValue())
                throw new AssertionError("requireNonNullElse(null, null) is expected to throw NullPointerException.");
        });
        writer.write("Testing equals:\n");
        writer.scope(() -> {
            if (!tester.equals(null, null)) {
                throw new AssertionError("equals(null, null) is expected to be true.");
            }
            if (tester.equals(null, new Object())) {
                throw new AssertionError("equals(null, new Object()) is expected to be false.");
            }
            if (tester.equals(new Object(), null)) {
                throw new AssertionError("equals(new Object(), null) is expected to be false.");
            }
            if (tester.equals(new Object(), new Object())) {
                throw new AssertionError("equals(new Object(), new Object()) is expected to be false.");
            }
            if (!tester.equals("abc", "abc")) {
                throw new AssertionError("equals(\"abc\", \"abc\") is expected to be true.");
            }
            if (!tester.equals(Set.of("abc", "def"), Set.of("def", "abc"))) {
                throw new AssertionError(
                        "equals(Set.of(\"abc\", \"def\"), Set.of(\"def\", \"abc\")) is expected to be true."
                );
            }
        });
        writer.write("Testing toString:\n");
        writer.scope(() -> {
            writer.write("Testing toString(Object):\n");
            writer.scope(() -> {
                if (!tester.toString((Object) null).equals("null")) {
                    throw new AssertionError("Expected toString(null) to be equal to \"null\".");
                }
                if (!tester.toString(123).equals("123")) {
                    throw new AssertionError("Expected toString(123) to be equal to \"123\".");
                }
                final Object obj = new Object();
                if (!tester.toString(obj).equals(obj.toString())) {
                    throw new AssertionError("Expected toString(new Object()) to be equal to this object.toString().");
                }
                final Object arr = new int[10];
                if (!tester.toString(arr).equals(arr.toString())) {
                    throw new AssertionError("Expected toString((Object) new int[10]) to be equal to [I@address.");
                }
            });
            writer.write("Testing toString(Object, String):\n");
            writer.scope(() -> {
                if (!tester.toString("hello", "world").equals("hello")) {
                    throw new AssertionError("Expected toString(\"hello\", \"world\") to be equal to hello.");
                }
                if (!tester.toString("AMOGUS", null).equals("AMOGUS")) {
                    throw new AssertionError("Expected toString(\"AMOGUS\", null) to be equal to AMOGUS.");
                }
                Object obj = new Object();
                if (!tester.toString(obj, null).equals(obj.toString())) {
                    throw new AssertionError(
                            "Expected toString(new Object(), null) to be equal to java.lang.Object@address.");
                }
                if (tester.toString(null, null) != null) {
                    throw new AssertionError(
                            "Expected toString(null, null) to be equal to null.");
                }
                if (tester.toString(null, "null-default-abacaba") != "null-default-abacaba") {
                    throw new AssertionError(
                            "Expected toString(null, \"null-default-abacaba\") to be equal to \"null-default-abacaba\".");
                }
            });
            writer.write("Testing toString(Object[]):\n");
            writer.scope(() -> {
                if (!tester.toString((Object[]) null).equals("null")) {
                    throw new AssertionError("Expected toString(null) to be equal to \"null\".");
                }
                if (!tester.toString(new Integer[0]).equals("[]")) {
                    throw new AssertionError("Expected toString(new Integer[0]) to be equal to \"[]\".");
                }
                if (!tester.toString(new Object[]{"hello", "cruel", "world"}).equals("[hello, cruel, world]")) {
                    throw new AssertionError(
                            "Expected toString(new Object[]{\"hello\", \"cruel\", \"world\"}) to be equal to [hello, cruel, world].");
                }
                if (!tester.toString(new Object[]{"hello", 11, null}).equals("[hello, 11, null]")) {
                    throw new AssertionError(
                            "Expected toString(new Object[]{\"hello\", 11, null}) to be equal to [hello, 11, null].");
                }
                Object obj = new Object();
                int[] ints = new int[10];
                String[] string = new String[3];
                if (!tester.toString(new Object[]{obj, null, ints, string}).equals(String.format("[%s, %s, %s, %s]", obj, null, ints, string))) {
                    throw new AssertionError(
                            "Expected toString(new Object[]{new Object(), null, new int[10], new String[3]}) to be equal" +
                                    " to [java.lang.Object@???, null, [I@???, [Ljava.lang.String;@???].");
                }
            });
        });
    }
}

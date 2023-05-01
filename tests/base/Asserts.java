package base;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Asserts {
    private Asserts() {
    }

    public static void assertTrue(final boolean condition, final String message) {
        assertFalse(!condition, message);
    }

    public static void assertFalse(final boolean condition, final String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(final Object o1, final Object o2, final String message) {
        assertTrue(Objects.equals(o1, o2), message);
    }

    public static void assertIdentical(final Object o1, final Object o2, final String message) {
        assertTrue(o1 == o2, message);
    }

    public static void assertNotIdentical(final Object o1, final Object o2, final String message) {
        assertTrue(o1 != o2, message);
    }

    public static void assertNull(final Object obj, final String message) {
        assertIdentical(obj, null, message);
    }

    public static void assertThrow(final Runnable runnable, final Class<? extends Exception> exceptionType, final String message) {
        try {
            runnable.run();
        } catch (final RuntimeException e) {
            if (exceptionType.isInstance(e)) {
                return;
            } else {
                throw e;
            }
        }
        throw new AssertionError(message);
    }

    public static void assertEmpty(final Collection<?> collection, final String message) {
        assertTrue(collection.isEmpty(), message);
    }

    public static void assertThrow(final Runnable runnable, final List<Class<? extends Exception>> exceptionTypes, final String message) {
        try {
            runnable.run();
        } catch (final RuntimeException e) {
            for (Class<? extends Exception> exceptionType : exceptionTypes) {
                if (exceptionType.isInstance(e)) {
                    return;
                }
            }
            throw e;
        }
        throw new AssertionError(message);
    }
}

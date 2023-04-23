package collections;

import java.lang.ref.Cleaner;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class TestClassBase {
    protected final String value;
    static final Set<WeakReference<TestClassBase>> testClasses = new HashSet<>();
    final WeakReference<TestClassBase> selfReference;

    protected TestClassBase(final String value) {
        this.value = Objects.requireNonNull(value);

        selfReference = new WeakReference<>(this);
        testClasses.add(selfReference);

        final WeakReference<TestClassBase> selfReferenceCopy = selfReference;
        Cleaner.create().register(this, () -> testClasses.remove(selfReferenceCopy));
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof TestClassBase that) {
            return value.equals(that.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "<" + value + ">";
    }
}


/**
 * Какой-то класс, для которого корректно определён {@link #equals(Object)}. Больше свойств о нём не известно.
 */
public class TestClass extends TestClassBase {
    TestClass(final String value) {
        super(value);
    }

    TestClassBase copyBase() {
        return new TestClassBase(value);
    }

    @Override
    public String toString() {
        return "{" + value + '}';
    }
}

package collections;

import java.lang.ref.Cleaner;
import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToIntFunction;

class TestClassBase {
    final String value;
    static final Set<WeakReference<TestClassBase>> testClasses = new HashSet<>();
    final WeakReference<TestClassBase> selfReference;

    TestClassBase(final String value) {
        this.value = Objects.requireNonNull(value);

        selfReference = new WeakReference<>(this);
        testClasses.add(selfReference);

        final WeakReference<TestClassBase> selfReferenceCopy = selfReference;
        Cleaner.create().register(this, () -> testClasses.remove(selfReferenceCopy));
    }

    String getValue() {
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
 * Какой-то класс, для которого корректно определены {@link #equals(Object)} и {@link #hashCode()}.
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

    private static final ToIntFunction<TestClass> inheritanceToInt = testClass -> testClass instanceof TestClassDerived ? 100 : 0;
    static final Comparator<TestClass> SENSITIVE_COMPARATOR = Comparator.nullsFirst(Comparator.comparing(TestClass::getValue).thenComparingInt(inheritanceToInt));
    static final Comparator<TestClass> LENGTH_COMPARATOR = Comparator.comparingInt(o -> o.getValue().length());
}

class TestClassDerived extends TestClass {

    TestClassDerived(final String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "(" + value + ')';
    }
}

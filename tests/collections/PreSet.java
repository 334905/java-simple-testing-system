package collections;

/**
 * Пред-множество. Содержит некое подмножество методов {@link java.util.Set}, чтобы было проще реализовывать.
 * Хранит объекты типа {@link TestClass} для простоты тестирования.
 */
public interface PreSet extends PreCollection {
    /**
     * Добавляет в множество указанный элемент.
     * В множестве есть элемент, {@link java.util.Objects#equals(Object, Object) равный} данному, множество остаётся неизменным.
     * @param element Элемент, который надо добавить.
     * @return {@code true}, если элемент был добавлен, иначе {@code false}.
     */
    @Override
    boolean add(TestClass element);
}

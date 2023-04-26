package collections;

import java.util.ListIterator;

/**
 * Сложный пред-список. Содержит некое подмножество методов {@link java.util.List}, чтобы было проще реализовывать.
 * Набор методов включает в себя (но не ограничивается) {@link SimplePreList простым пред-списком}.
 * Хранит объекты типа {@link TestClass} для простоты тестирования.
 */
public interface ComplexPreList extends SimplePreList {
    /**
     * Проверяет, есть ли объект, {@link java.util.Objects#equals(Object, Object) равный} данному в списке и возвращает <b>последний индекс</b>, по которому он находится.
     * @param element Элемент, который нужно искать в списке.
     * @return <b>Последний</b> индекс, по которому находится данный элемент, либо {@code -1}, если такого элемента в списке нет.
     */
    int lastIndexOf(Object element);

    /**
     * Возвращает {@link ListIterator}, итерирующийся по всем элементам данного списка по порядку.
     * @return Итератор, итерирующийся по всем элементам данного списка.
     * @see ListIterator
     */
    ListIterator<TestClass> listIterator();

    /**
     * Возвращает {@link ListIterator}, итерирующийся по элементам данного списка по порядку начиная с заданного и до конца.
     * @param index Индекс элемента, который должен быть возвращён при первом вызове {@link ListIterator#next()}.
     * @return Итератор, итерирующийся по элементам данного списка от заданного места и до конца.
     * @see ListIterator
     * @throws IndexOutOfBoundsException Если индекс отрицателен или больше чем {@link #size()}.
     */
    ListIterator<TestClass> listIterator(int index);

    /**
     * Возвращает <i>вид</i> на часть заданного списка начиная с индекса {@code fromIndex} (включительно) до {@code toIndex} (не включительно).
     * Если {@code fromIndex} равен {@code toIndex}, искомый список пуст.
     * <p>
     * <i>Видом</i> на часть списка является список, изменение которого приводит к соответствующему изменению оригинального списка.
     * Например, следующий код удаляет все элементы из промежутка от {@code from} (включительно) до {@code to} не включительно.
     * <pre>
     *     list.subList(from, to).clear();
     * </pre>
     * Обратное верно лишь отчасти: при изменении элемента оригинального списка, аналогичных элемент <i>вида</i> меняется.
     * При изменении структуры оригинального списка, не специфицировано, что должно произойти с <i>видом</i> на список.
     * В частности, если взято два <i>вида</i> на части списка (не важно, одинаковые или разные), структурное изменение одного влечёт инвалидацию другого.
     * @param fromIndex Индекс начала <i>вида</i> на список (включительно).
     * @param toIndex Индекс конца <i>вида</i> на список (включительно).
     * @return Вид на список с указанными свойствами.
     * @throws IndexOutOfBoundsException если индекс начала отрицателен, индекс конца больше {@link #size()} или индекс начала больше индекса конца.
     */
    ComplexPreList subList(int fromIndex, int toIndex);
}
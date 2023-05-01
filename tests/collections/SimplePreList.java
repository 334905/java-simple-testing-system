package collections;

import java.util.Iterator;

/**
 * Простой пред-список. Содержит некое подмножество методов {@link java.util.List}, чтобы было проще реализовывать.
 * Хранит объекты типа {@link TestClass} для простоты тестирования.
 */
public interface SimplePreList extends PreCollection {
    /**
     * Добавляет в конец списка указанный элемент.
     * @param element Элемент, который надо добавить.
     * @return {@code true}.
     */
    @Override
    boolean add(TestClass element);

    /**
     * Добавляет указанный элемент в заданное место списка.
     * @param index Индекс, на котором элемент должен оказаться после вставки.
     * @param element Элемент, который надо добавить.
     * @throws IndexOutOfBoundsException если индекс меньше нуля или больше {@link #size()}.
     */
    void add(int index, TestClass element);

    /**
     * Возвращает элемент списка, находящийся по заданному индексу.
     * @param index Индекс, по которому надо получить элемент.
     * @return Элемент списка, находящийся по заданному индексу.
     * @throws IndexOutOfBoundsException Если индекс меньше нуля или больше либо равен {@link #size()}.
     */
    TestClass get(int index);

    /**
     * Проверяет, есть ли объект, {@link java.util.Objects#equals(Object, Object) равный} данному в списке и возвращает <b>первый индекс</b>, по которому он находится.
     * @param element Элемент, который нужно искать в списке.
     * @return <b>Первый</b> индекс, по которому находится данный элемент, либо {@code -1}, если такого элемента в списке нет.
     */
    int indexOf(Object element);

    /**
     * Возвращает итератор, итерирующийся по всем элементам данного списка по порядку.
     * @return Итератор, итерирующийся по всем элементам данного списка.
     */
    @Override
    Iterator<TestClass> iterator();

    /**
     * Удаляет из списка <b>первый</b> элемент, {@link java.util.Objects#equals(Object, Object) равный} данному.
     * Не меняет список, если такого элемента в списке нет.
     * @param element Элемент, равный которому надо удалить.
     * @return {@code true}, если элемент был удалён из списка, {@code false} иначе.
     */
    boolean remove(Object element);

    /**
     * Удаляет из списка элемент, находящийся по заданному индексу.
     * @param index Индекс, элемент находящийся по которому должен быть удалён.
     * @return Элемент, находившийся по заданному индексу.
     * @throws IndexOutOfBoundsException Если индекс меньше нуля или больше либо равен {@link #size()}.
     */
    TestClass remove(int index);

    /**
     * Присваивает элементу по заданному индексу заданное значение.
     * @param index Индекс, элемент по которому должен быть изменён.
     * @param element Новое значение элемента списка.
     * @return Значение, которое раньше лежало по указанному индексу.
     * @throws IndexOutOfBoundsException Если индекс меньше нуля или больше либо равен {@link #size()}.
     */
    TestClass set(int index, TestClass element);
}

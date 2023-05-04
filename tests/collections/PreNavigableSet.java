package collections;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Collections;
import java.util.Iterator;

/**
 * Отсортированное пред-множество. Содержит некое подмножество методов {@link java.util.NavigableSet}, чтобы было проще реализовывать.
 * Хранит объекты типа {@link TestClass} для простоты тестирования.
 * <p>
 * Спойлер: не смотрите на такое большое количество методов: что-то содержательное придётся писать только в методах
 * <ul>
 *     <li> {@link #lower(TestClass)}, {@link #higher(TestClass)}, {@link #floor(TestClass)}, {@link #ceiling(TestClass)} (которые очевидным образом реализуются через один общий).
 *     <li> {@link #pollFirst()} и {@link #pollLast()} (хотя их содержательность под большим вопросом).
 *     <li> {@link #descendingSet()}.
 *     <li> {@link #subSet(TestClass, boolean, TestClass, boolean)}.
 * </ul>
 */
public interface PreNavigableSet extends PreSet {
    /**
     * Возвращает {@link Comparator компаратор}, который используется для упорядочивания элементов в множестве.
     *
     * @return {@link Comparator компаратор}, который используется для упорядочивания элементов в множестве.
     */
    Comparator<TestClass> comparator();

    /**
     * Возвращает первый (наименьший) элемент, который есть в множестве.
     *
     * @return первый (наименьший) элемент, который есть в множестве
     * @throws NoSuchElementException если множество пусто
     */
    TestClass first();
    /**
     * Возвращает последний (наибольший) элемент, который есть в множестве.
     *
     * @return последний (наибольший) элемент, который есть в множестве
     * @throws NoSuchElementException если множество пусто
     */
    TestClass last();

    /**
     * Возвращает самый большой элемент из множества, строго меньший данного или {@code null}, если такого нет.
     *
     * @param e элемент, предыдущий к которому надо искать
     * @return самый большой элемент из множества, строго меньший данного или {@code null}, если такого нет
     * @throws NullPointerException Если указанный элемент {@code null}, а компаратор не приемлит {@code null}'ы.
     */
    TestClass lower(TestClass e);

    /**
     * Возвращает самый маленький элемент из множества, строго больший данного или {@code null}, если такого нет.
     *
     * @param e элемент, следующий к которому надо искать
     * @return самый маленький элемент из множества, строго больший данного или {@code null}, если такого нет
     * @throws NullPointerException Если указанный элемент {@code null}, а компаратор не приемлит {@code null}'ы.
     */
    TestClass higher(TestClass e);

    /**
     * Возвращает самый большой элемент из множества, меньший либо равный данному или {@code null}, если такого нет.
     *
     * @param e элемент, предыдущий (нестрого) к которому надо искать
     * @return самый большой элемент из множества, меньший либо равный данному или {@code null}, если такого нет
     * @throws NullPointerException Если указанный элемент {@code null}, а компаратор не приемлит {@code null}'ы.
     */
    TestClass floor(TestClass e);

    /**
     * Возвращает самый маленький элемент из множества, больший либо равный данного или {@code null}, если такого нет.
     *
     * @param e элемент, следующий (нестрого) к которому надо искать
     * @return самый маленький элемент из множества, больший либо равный данного или {@code null}, если такого нет
     * @throws NullPointerException Если указанный элемент {@code null}, а компаратор не приемлит {@code null}'ы.
     */
    TestClass ceiling(TestClass e);

    /**
     * Получает <b>и удаляет</b> первый (наименьший) элемент, который есть в множестве или {@code null}, если множество пусто.
     *
     * @return первый (наименьший) элемент, который есть в множестве или {@code null}, если множество пусто
     */
    TestClass pollFirst();

    /**
     * Получает <b>и удаляет</b> последний (наибольший) элемент, который есть в множестве или {@code null}, если множество пусто.
     *
     * @return последний (наибольший) элемент, который есть в множестве или {@code null}, если множество пусто.
     */
    TestClass pollLast();

    /**
     * Возвращает <b>вид</b> на множество в обратном порядке,
     * то есть такой вид, в котором все элементы расположены {@link Collections#reverseOrder(Comparator) в обратном порядке}.
     * Любое изменение оригинального множества приводит к соответствующему изменению вида, и наоборот.
     *
     * @return вид на множество в обратном порядке
     */
    PreNavigableSet descendingSet();

    /**
     * Возвращает итератор по элементам множества в обратном порядке.
     * @return итератор по элементам множества в обратном порядке
     * @see #descendingSet()
     */
    Iterator<TestClass> descendingIterator();

    /**
     * Возвращает <b>вид</b> на часть множества, состоящую только из тех элементов, которые лежат в указанном диапазоне.
     * Любое изменение оригинального множества отражается на виде и наоборот.
     * Попытка добавить в вид элемент, который не лежит в указанном диапазоне, приводит к исключению {@link IllegalArgumentException}.
     * @param fromElement нижняя граница диапазона
     * @param fromInclusive {@code true}, если {@code fromElement} принадлежит диапазону, {@code false} иначе
     * @param toElement верхняя граница диапазона
     * @param toInclusive {@code true}, если {@code toElement} принадлежит диапазону, {@code false} иначе
     * @return вид на часть множества от {@code fromElement} до {@code toElement}
     * @throws IllegalArgumentException если {@code fromElement} строго больше {@code toElement}
     * или если множество изначально является результатом {@link #subSet(TestClass, boolean, TestClass, boolean) subSet}, {@link #headSet(TestClass, boolean) headSet} или аналогов,
     * а {@code fromElement} и/или {@code toElement} лежат вне допустимого диапазона элементов.
     * @throws NullPointerException если {@code fromElement} или {@code toElement} является {@code null}'ом, а компаратор не поддерживает {@code null}'ы.
     */
    PreNavigableSet subSet(TestClass fromElement, boolean fromInclusive, TestClass toElement, boolean toInclusive);

    /**
     * Возвращает <b>вид</b> на часть множества, состоящую только из тех элементов, которые меньше (или меньше либо равны, в зависимости от {@code inclusive}) указанного элемента.
     * Любое изменение оригинального множества отражается на виде и наоборот.
     * Попытка добавить в вид элемент, который не лежит в указанном диапазоне, приводит к исключению {@link IllegalArgumentException}.
     * @param toElement верхняя граница диапазона
     * @param inclusive {@code true}, если {@code toElement} принадлежит диапазону, {@code false} иначе
     * @return вид на часть множества от начала до {@code toElement}
     * @throws IllegalArgumentException если множество изначально является результатом {@link #subSet(TestClass, boolean, TestClass, boolean) subSet}, {@link #headSet(TestClass, boolean) headSet} или аналогов,
     * а {@code toElement} лежит вне допустимого диапазона элементов.
     * @throws NullPointerException если {@code toElement} является {@code null}'ом, а компаратор не поддерживает {@code null}'ы.
     */
    PreNavigableSet headSet(TestClass toElement, boolean inclusive);

    /**
     * Возвращает <b>вид</b> на часть множества, состоящую только из тех элементов, которые больше (или больше либо равны, в зависимости от {@code inclusive}) указанного элемента.
     * Любое изменение оригинального множества отражается на виде и наоборот.
     * Попытка добавить в вид элемент, который не лежит в указанном диапазоне, приводит к исключению {@link IllegalArgumentException}.
     * @param fromElement нижняя граница диапазона
     * @param inclusive {@code true}, если {@code fromElement} принадлежит диапазону, {@code false} иначе
     * @return вид на часть множества от {@code fromElement} до конца
     * @throws IllegalArgumentException если множество изначально является результатом {@link #subSet(TestClass, boolean, TestClass, boolean) subSet}, {@link #headSet(TestClass, boolean) headSet} или аналогов,
     * а {@code fromElement} лежит вне допустимого диапазона элементов.
     * @throws NullPointerException если {@code fromElement} является {@code null}'ом, а компаратор не поддерживает {@code null}'ы.
     */
    PreNavigableSet tailSet(TestClass fromElement, boolean inclusive);

    /**
     * Возвращает <b>вид</b> на часть множества, состоящую только из тех элементов, которые лежат в указанном диапазоне.
     * Любое изменение оригинального множества отражается на виде и наоборот.
     * Попытка добавить в вид элемент, который не лежит в указанном диапазоне, приводит к исключению {@link IllegalArgumentException}.
     * <p>
     * Аналогично следующему вызову {@link #subSet(TestClass, boolean, TestClass, boolean)}: {@code subSet(fromElement, true, toElement, false)}.
     * @param fromElement нижняя граница диапазона (включительно)
     * @param toElement верхняя граница диапазона (исключительно)
     * @return вид на часть множества от {@code fromElement} (включительно) до {@code toElement} (исключительно)
     * @throws IllegalArgumentException если {@code fromElement} строго больше {@code toElement}
     * или если множество изначально является результатом {@link #subSet(TestClass, boolean, TestClass, boolean) subSet}, {@link #headSet(TestClass, boolean) headSet} или аналогов,
     * а {@code fromElement} и/или {@code toElement} лежат вне допустимого диапазона элементов.
     * @throws NullPointerException если {@code fromElement} или {@code toElement} является {@code null}'ом, а компаратор не поддерживает {@code null}'ы.
     */
    PreNavigableSet subSet(TestClass fromElement, TestClass toElement);

    /**
     * Возвращает <b>вид</b> на часть множества, состоящую только из тех элементов, которые строго меньше указанного элемента.
     * Любое изменение оригинального множества отражается на виде и наоборот.
     * Попытка добавить в вид элемент, который не лежит в указанном диапазоне, приводит к исключению {@link IllegalArgumentException}.
     * <p>
     * Аналогично следующему вызову {@link #headSet(TestClass, boolean)}: {@code headSet(toElement, false)}.
     * @param toElement верхняя граница диапазона (исключительно)
     * @return вид на часть множества от начала до {@code toElement} исключительно
     * @throws IllegalArgumentException если множество изначально является результатом {@link #subSet(TestClass, boolean, TestClass, boolean) subSet}, {@link #headSet(TestClass, boolean) headSet} или аналогов,
     * а {@code toElement} лежит вне допустимого диапазона элементов.
     * @throws NullPointerException если {@code toElement} является {@code null}'ом, а компаратор не поддерживает {@code null}'ы.
     */
    PreNavigableSet headSet(TestClass toElement);

    /**
     * Возвращает <b>вид</b> на часть множества, состоящую только из тех элементов, которые больше либо равны указанному элементу.
     * Любое изменение оригинального множества отражается на виде и наоборот.
     * Попытка добавить в вид элемент, который не лежит в указанном диапазоне, приводит к исключению {@link IllegalArgumentException}.
     * <p>
     * Аналогично следующему вызову {@link #tailSet(TestClass, boolean)}: {@code tailSet(fromElement, true)}.
     * @param fromElement нижняя граница диапазона (включительно)
     * @return вид на часть множества от {@code fromElement} включительно до конца
     * @throws IllegalArgumentException если множество изначально является результатом {@link #subSet(TestClass, boolean, TestClass, boolean) subSet}, {@link #headSet(TestClass, boolean) headSet} или аналогов,
     * а {@code fromElement} лежит вне допустимого диапазона элементов.
     * @throws NullPointerException если {@code fromElement} является {@code null}'ом, а компаратор не поддерживает {@code null}'ы.
     */
    PreNavigableSet tailSet(TestClass fromElement);
}

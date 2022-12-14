# Введение.
Вам предоставляются тесты, чтобы можно было легко проверить правильность ваших решений.

Самый простой (хоть и не самый правильный) способ запустить тесты корректно — положить ваш файл рядом с тестирующим, скомпилировать оба и запустить тесты.

Описание всех практик и домашних заданий продублированы сюда.

# Практика 1. `Sorting`.
Параметрами командной строки задан массив строк. Отсортируйте их лексикографически.
1. Напоминаю, параметры командной строки — это `args` в строке `void main(String[] args)`.
2. Каждая строка состоит из маленьких латинских букв. Каждая строка непуста.
3. Лексикографический порядок — это следующий порядок:
   - Пустая строка
   - `a`
   - `aa`
   - `aaa`
   - `aab`
   - `aac`
   - ...
   - `aaz`
   - `ab`
   - `aba`
   - `abb`
   - ...
   - `abz`
   - `ac`
   - ... ...
   - `az`
   - `aza`
   - `azb`
   - ...
   - `azz`
   - `b`
   - `ba`
   - `baa`
   - ...

   То есть если есть две строки, то
   1. Если строки совпадают до *i*-того символа, а *i*+1-й символ различается, то раньше идёт та строка, у которой этот символ раньше в алфавите.
   2. Если строки совпадают до *i*-того символа, и на *i*-м символе одна строка кончается, а вторая — нет, то раньше идёт та строка, которая кончается.
4. В Java символы (`char`) можно сравнивать при помощи операций `<`, `>`, `<=` и `>=`. Считается, что `'a' < 'b' < 'c' < ... < 'z'`.
5. В Java строки (`String`) при помощи `<`, `>`, `<=` и `>=` не сравниваются. Сравниваются некоторым другим способом, но для данного задания лучше напишите сравнение сами.
6. Ответ (отсортированный массив) следует выводить по одной строке в каждой строке. 
7. Файл с тестами называется `SortingTest.java`.

# ДЗ 1. `Sum`.
Параметрами командной строки задан набор целых чисел, вмещающихся в тип данных `int`. Требуется посчитать их сумму и вывести её.
1. Каждый аргумент содержит числа, разделённые произвольным количеством произвольных [пробельных символов](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Character.html#isWhitespace%28char%29). Каждое число состоит из цифр, а также может содержать `-` или `+` в начале. **Внимание**: в Java цифрами считаются не только символы от `0` до `9`. Например, ваша программа должна воспринять `४৬` как `46`. 
2. Складывать числа надо в типе `int`.
3. От вас не требуется умение разбирать числа руками. Зато требуется умение читать документацию: в классах [`String`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html) и [`Integer`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Integer.html) находится много методов, некоторые из которых будут вам полезны.
4. Если Вы хотите выводить что-то для себя, а не для проверяющей системы, используйте [`System.err`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#err) (например, `System.err.println(...)`).
5. Примеры входных и выходных данных:

|Запуск|Вывод|
|------|:---:|
|`java Sum 1 2 3`|`6`|
|`java Sum 1 2 -3`|`0`|
|`java Sum "1 2 3"`|`6`|
|`java Sum "1 +2" " 3"`|`6`|
|`java Sum "  "`|`0`|
|`java Sum 2147483647 1`|`-2147483648`|

## Модификация. `SumBigInteger`.
Сумма чисел должна считаться точно, а не по правилам округления типа `int`.
1. Перед реализацией рекомендуется ознакомиться с [документацией класса `BigInteger`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigInteger.html).
2. Примеры входных и выходных данных:

|Запуск|Вывод|
|------|:---:|
|`java SumBigInteger 2147483647 1`|`2147483648`|
|`java SumBigInteger 9223372036854775808 +1`|`9223372036854775809`|
|`java SumBigInteger "-9223372036854775809 -9223372036854775808"`|`-18446744073709551617`|
|`java SumBigInteger 340282366920938463463374607431768211456`|`340282366920938463463374607431768211456`|

# ДЗ 2. `Reverse`.
Из стандартного ввода ваше программе подаются строки с числами. Вашей задачей является вывести те же самые числа, но в ином порядке: порядок чисел в каждой строке должен быть заменён на обратный, порядок самих строк — тоже.
1. Вход содержит не более 10⁶ чисел и строк.
2. Ввод может содержать пустые строки. Они должны быть сохранены в ответе.
3. Числа разделены пробелами. Сохранять количество пробелов не требуется.
4. Для чтения чисел используйте класс [`Scanner`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Scanner.html).
5. Примеры работы программы:

```
+--------+-----+
|  Ввод  |Вывод|
+--------+-----+
|1 2     |3    |
|3       |2 1  |
+--------+-----+
|3       |1 2  |
|2 1     |3    |
+--------+-----+
|1       |-3 2 |
|        |     |
|2 -3    |1    |
+--------+-----+
|1     2 |4 3  |
|3     4 |2 1  |
+--------+-----+
```
**Примечание**: `Scanner` тормозит. Пока с этим ничего не сделать.

## Модификация. `ReverseEven`.
- Пусть _M_ — суммарное количество строк и чисел во входном файле.
  Тогда Ваша программа должна использовать не более 32 * _M_ + 1024 байтов памяти.
- Выводите (в перевёрнутом порядке) только нечётные числа.
- Примеры работы программы:

```
+----------+-----+
|   Ввод   |Вывод|
+----------+-----+
|1 2 3 4 5 |13 11|
|6 7 8 10  |7    |
|          |     |
|11 13     |5 3 1|
+----------+-----+
```


# Практика 2. `SimpleWordStat`.
1. Словом называется то, что словом считает `Scanner`. Для подсчета статистики слова приводятся к нижнему регистру.
2. Выходной файл должен содержать все различные слова, встречающиеся во входном файле, в порядке уменьшения их количества. Для каждого слова должна быть выведена одна строка, содержащая слово и число его вхождений во входном файле.
3. Имена входного и выходного файла задаются в качестве аргументов командной строки. Кодировка файлов: UTF-8.
4. Для вывода в файл следует использовать класс [`PrintStream`](https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/io/PrintStream.html), имеющий тот же набор методов, что и `System.out`. 
5. Примеры работы программы:

Входной файл
```
To be, or not to be, that is the question:
```
Выходной файл
```
to 2
be, 2
or 1
not 1
that 1
is 1
the 1
question: 1
```

# ДЗ 3. `WordStatScanned`.
Посчитайте количество раз, которое каждое слово встречается во входном файле.
1. Словом называется непрерывная последовательность непробельных символов. Для подсчета статистики слова приводятся к нижнему регистру.
2. Выходной файл должен содержать все различные слова, встречающиеся во входном файле, в порядке их первой встречи во входном файле. Для каждого слова должна быть выведена одна строка, содержащая слово и число его вхождений во входном файле.
3. Имена входного и выходного файла задаются в качестве аргументов командной строки. Кодировка файлов: UTF-8.
4. Для чтения файла следует использовать класс [`Scanner`](https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/util/Scanner.html).
5. Для вывода в файл следует использовать класс [`PrintStream`](https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/io/PrintStream.html).
6. В задании необходимо корректно обработать все ошибки (**не только все исключения**), связанные с вводом-выводом. Для каждой ошибки требуется вывести максимально подробное человекочитаемое сообщение об ошибке в [стандартный поток ошибок](https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/lang/System.html#err). Ошибки, произошедшие при записи сообщения в стандартный поток ошибок, можно не обрабатывать.  
7. Примеры работы программы:

Входной файл
```
To be, or not to be, that is the question:
```
Выходной файл
```
to 2
be, 2
or 1
not 1
that 1
is 1
the 1
question: 1
```

Входной файл:
```
Monday's child is fair of face.
Tuesday's child is full of grace.
```
Выходной файл:
```
monday's 1
child 2
is 2
fair 1
of 2
face. 1
tuesday's 1
full 1
grace. 1
```

Входной файл:
```
Шалтай-Болтай
Сидел на стене.
Шалтай-Болтай
Свалился во сне.
```
Выходной файл:
```
шалтай-болтай 2
сидел 1
на 1
стене. 1
свалился 1
во 1
сне. 1
```

## Модификация. `WordStatLexical`.
Слова в выходном файле должны быть отсортированы в лексикографическом порядке, а не в порядке встречи во входном файле.

**Подсказка**: Java по умолчанию сортирует строки в лексокографическом порядке.

Примеры работы программы:

Входной файл
```
To be, or not to be, that is the question:
```
Выходной файл
```
be, 2
is 1
not 1
or 1
question: 1
that 1
the 1
to 2
```

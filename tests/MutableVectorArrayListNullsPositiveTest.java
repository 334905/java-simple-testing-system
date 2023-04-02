import base.ExtendedRandom;
import base.IndentingWriter;
import mutable_vector_array_list.MutableVectorArrayListTester;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class MutableVectorArrayListNullsPositiveTest extends MutableVectorArrayListTest {
    public static void main(String[] args) throws Exception {
        MutableVectorArrayListTest.main(
                new MutableVectorArrayListTester(),
                new IndentingWriter(new BufferedWriter(new OutputStreamWriter(System.out))),
                new ExtendedRandom(),
                true);
    }
}

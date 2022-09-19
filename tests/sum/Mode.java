package sum;

import java.util.Random;

public interface Mode<T extends Number> {
    T parseNumber(String str);
    T zero();
    T add(T left, T right);
    T random(Random rng, T origin, T bound);
}

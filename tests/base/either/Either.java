package base.either;

public sealed abstract class Either<A, B> {
    public abstract int index();
    public abstract A getFirst();
    public abstract B getSecond();

    public static <T, B> Either<T, B> ofSecond(final B value) {
        return new EitherSecond<>(value);
    }

    public static <A, B> Either<A, B> ofFirst(final A value) {
        return new EitherFirst<>(value);
    }

    private static final class EitherFirst<A, B> extends Either<A, B> {
        private final A value;

        public EitherFirst(final A value) {
            this.value = value;
        }

        @Override
        public int index() {
            return 1;
        }

        @Override
        public A getFirst() {
            return value;
        }

        @Override
        public B getSecond() {
            throw new BadEitherAccessException("Trying to get second value while holding first " + value);
        }
    }

    private static final class EitherSecond<T, B> extends Either<T, B> {
        private final B value;

        public EitherSecond(final B value) {
            this.value = value;
        }

        @Override
        public int index() {
            return 0;
        }

        @Override
        public T getFirst() {
            throw new BadEitherAccessException("Trying to get first value while holding second " + value);
        }

        @Override
        public B getSecond() {
            return value;
        }
    }
}

package base.pairs;

import java.util.Iterator;

public class PairIterator<F, S> implements Iterator<Pair<F, S>> {
    private final Iterator<F> first;
    private final Iterator<S> second;

    public PairIterator(final Iterator<F> first, final Iterator<S> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean hasNext() {
        return first.hasNext() && second.hasNext();
    }

    @Override
    public Pair<F, S> next() {
        return Pair.of(first.next(), second.next());
    }

    @Override
    public void remove() {
        first.remove();
        second.remove();
    }
}

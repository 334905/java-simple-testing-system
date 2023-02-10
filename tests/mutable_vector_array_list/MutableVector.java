package mutable_vector_array_list;

import java.util.Objects;

public class MutableVector {
    private double x;
    private double y;

    public MutableVector(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public MutableVector(final MutableVector other) {
        this.x = other.x;
        this.y = other.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MutableVector that = (MutableVector) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "MutableVector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

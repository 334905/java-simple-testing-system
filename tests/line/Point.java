package line;

import java.util.Objects;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Point point = (Point) obj;
        return (x == point.x || Double.isNaN(x) && Double.isNaN(point.x)) &&
                (y == point.y || Double.isNaN(y) && Double.isNaN(point.y));
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

package pers.yzx.geometry;

import java.util.Objects;

public class Point {
    private final double x;
    private final double y;
    private final double z;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Point add(Vector vector) {
        return new Point(x + vector.x, y + vector.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point point = (Point) obj;
            return Double.compare(x, point.x) == 0 && Double.compare(y, point.y) == 0 && Double.compare(z, point.z) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "," + z + "]";
    }
}

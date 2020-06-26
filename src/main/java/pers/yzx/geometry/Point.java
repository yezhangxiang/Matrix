package pers.yzx.geometry;

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
    public String toString() {
        return "[" + x + ", " + y + "," + z + "]";
    }
}

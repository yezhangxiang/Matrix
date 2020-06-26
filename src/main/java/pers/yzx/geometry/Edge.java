package pers.yzx.geometry;

/**
 * edge
 */
public class Edge {
    private Point a;
    private Point b;

    /**
     * Constructor of the edge
     *
     * @param a The first vertex of the edge
     * @param b The second vertex of the edge
     */
    public Edge(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }
}
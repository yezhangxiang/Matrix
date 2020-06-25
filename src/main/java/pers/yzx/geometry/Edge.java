package pers.yzx.geometry;

/**
 * edge
 */
public class Edge {
    public Point a;
    public Point b;

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

}
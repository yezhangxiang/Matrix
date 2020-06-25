package pers.yzx.geometry;

import java.util.Arrays;

/**
 * triangle
 */
public class Triangle {

    public Point a;
    public Point b;
    public Point c;

    /**
     * Constructor of the 2D triangle
     *
     * @param a The first point of the triangle
     * @param b The second point of the triangle
     * @param c The third point of the triangle
     */
    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Tests if a 2D point lies inside this 2D triangle. See Real-Time Collision
     * Detection, chap. 5, p. 206.
     *
     * @param point The point to be tested
     * @return Returns true iff the point lies inside this 2D triangle
     */
    public boolean contains(Point point) {
        double pab = new Vector(point, a).pseudoCross(new Vector(b, a));
        double pbc = new Vector(point, b).pseudoCross(new Vector(c, b));

        if (!hasSameSign(pab, pbc)) {
            return false;
        }

        double pca = new Vector(point, c).pseudoCross(new Vector(a, c));

        return hasSameSign(pab, pca);
    }

    /**
     * Tests if a given point lies in the circumcircle of this triangle. Let the
     * triangle ABC appear in counterclockwise (CCW) order. Then when det &gt;
     * 0, the point lies inside the circumcircle through the three points a, b
     * and c. If instead det &lt; 0, the point lies outside the circumcircle.
     * When det = 0, the four points are cocircular. If the triangle is oriented
     * clockwise (CW) the result is reversed. See Real-Time Collision Detection,
     * chap. 3, p. 34.
     *
     * @param point The point to be tested
     * @return Returns true iff the point lies inside the circumcircle through
     * the three points a, b, and c of the triangle
     */
    public boolean isPointInCircumcircle(Point point) {
        double a11 = a.getX() - point.getX();
        double a21 = b.getX() - point.getX();
        double a31 = c.getX() - point.getX();

        double a12 = a.getY() - point.getY();
        double a22 = b.getY() - point.getY();
        double a32 = c.getY() - point.getY();

        double a13 = (a.getX() - point.getX()) * (a.getX() - point.getX()) + (a.getY() - point.getY()) * (a.getY() - point.getY());
        double a23 = (b.getX() - point.getX()) * (b.getX() - point.getX()) + (b.getY() - point.getY()) * (b.getY() - point.getY());
        double a33 = (c.getX() - point.getX()) * (c.getX() - point.getX()) + (c.getY() - point.getY()) * (c.getY() - point.getY());

        double det = a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a13 * a22 * a31 - a12 * a21 * a33
                - a11 * a23 * a32;

        if (isOrientedCCW()) {
            return det > 0.0d;
        }

        return det < 0.0d;
    }

    /**
     * Test if this triangle is oriented counterclockwise (CCW). Let A, B and C
     * be three 2D points. If det &gt; 0, C lies to the left of the directed
     * line AB. Equivalently the triangle ABC is oriented counterclockwise. When
     * det &lt; 0, C lies to the right of the directed line AB, and the triangle
     * ABC is oriented clockwise. When det = 0, the three points are colinear.
     * See Real-Time Collision Detection, chap. 3, p. 32
     *
     * @return Returns true iff the triangle ABC is oriented counterclockwise
     * (CCW)
     */
    public boolean isOrientedCCW() {
        double a11 = a.getX() - c.getX();
        double a21 = b.getX() - c.getX();

        double a12 = a.getY() - c.getY();
        double a22 = b.getY() - c.getY();

        double det = a11 * a22 - a12 * a21;

        return det > 0.0d;
    }

    /**
     * Returns true if this triangle contains the given edge.
     *
     * @param edge The edge to be tested
     * @return Returns true if this triangle contains the edge
     */
    public boolean isNeighbour(Edge edge) {
        return (a == edge.a || b == edge.a || c == edge.a) && (a == edge.b || b == edge.b || c == edge.b);
    }

    /**
     * Returns the point of this triangle that is not part of the given edge.
     *
     * @param edge The edge
     * @return The point of this triangle that is not part of the edge
     */
    public Point getNoneEdgeVertex(Edge edge) {
        if (a != edge.a && a != edge.b) {
            return a;
        } else if (b != edge.a && b != edge.b) {
            return b;
        } else if (c != edge.a && c != edge.b) {
            return c;
        }

        return null;
    }

    /**
     * Returns true if the given point is one of the vertices describing this
     * triangle.
     *
     * @param point The point to be tested
     * @return Returns true if the Vertex is one of the vertices describing this
     * triangle
     */
    public boolean hasVertex(Point point) {
        return a == point || b == point || c == point;
    }

    /**
     * Returns an EdgeDistancePack containing the edge and its distance nearest
     * to the specified point.
     *
     * @param point The point the nearest edge is queried for
     * @return The edge of this triangle that is nearest to the specified point
     */
    public EdgeDistancePack findNearestEdge(Point point) {
        EdgeDistancePack[] edges = new EdgeDistancePack[3];

        edges[0] = new EdgeDistancePack(new Edge(a, b),
                new Vector(computeClosestPoint(new Edge(a, b), point), point).mag());
        edges[1] = new EdgeDistancePack(new Edge(b, c),
                new Vector(computeClosestPoint(new Edge(b, c), point), point).mag());
        edges[2] = new EdgeDistancePack(new Edge(c, a),
                new Vector(computeClosestPoint(new Edge(c, a), point), point).mag());

        Arrays.sort(edges);
        return edges[0];
    }

    /**
     * Computes the closest point on the given edge to the specified point.
     *
     * @param edge  The edge on which we search the closest point to the specified
     *              point
     * @param point The point to which we search the closest point on the edge
     * @return The closest point on the given edge to the specified point
     */
    private Point computeClosestPoint(Edge edge, Point point) {
        Vector ab = new Vector(edge.b, edge.a);
        double t = new Vector(point, (edge.a)).dot(ab) / ab.dot(ab);

        if (t < 0.0d) {
            t = 0.0d;
        } else if (t > 1.0d) {
            t = 1.0d;
        }

        return edge.a.add(ab.mult(t));
    }

    /**
     * Tests if the two arguments have the same sign.
     *
     * @param a The first floating point argument
     * @param b The second floating point argument
     * @return Returns true iff both arguments have the same sign
     */
    private boolean hasSameSign(double a, double b) {
        return Math.signum(a) == Math.signum(b);
    }

    @Override
    public String toString() {
        return "Triangle2D[" + a + ", " + b + ", " + c + "]";
    }

}
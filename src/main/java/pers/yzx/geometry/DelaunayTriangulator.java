package pers.yzx.geometry;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of incremental Delaunay triangulation algorithm.only for 2D.
 */
public class DelaunayTriangulator {
    private Set<Point> pointSet;
    private TriangleSoup triangleSoup;
    private Triangle superTriangle;

    /**
     * Constructor of the SimpleDelaunayTriangulator class used to create a new
     * triangulator instance.
     *
     * @param pointSet The point set to be triangulated
     */
    public DelaunayTriangulator(Set<Point> pointSet) {
        this.pointSet = pointSet;
        this.triangleSoup = new TriangleSoup();
    }

    /**
     * This method generates a Delaunay triangulation from the specified point
     * set.
     *
     * @throws IllegalArgumentException Thrown when the point set contains less than three points
     */
    public void triangulate() throws IllegalArgumentException {
        this.triangleSoup = new TriangleSoup();
        if (pointSet == null || pointSet.size() < 3) {
            throw new IllegalArgumentException("Less than three points in point set.");
        }
        superTriangle = generateSuperTriangle();
        triangleSoup.add(superTriangle);
        for (Point point : pointSet) {
            insertOnePoint(point);
        }
    }

    private void insertOnePoint(Point point) {
        Triangle triangle = triangleSoup.findContainingTriangle(point);
        if (triangle == null) {
            pointOnEdge(point);
        } else {
            pointInsideTriangle(point, triangle);
        }
    }

    /**
     * The vertex is inside a triangle.
     */
    private void pointInsideTriangle(Point point, Triangle triangle) {
        Point a = triangle.a;
        Point b = triangle.b;
        Point c = triangle.c;

        triangleSoup.remove(triangle);

        Triangle first = new Triangle(a, b, point);
        Triangle second = new Triangle(b, c, point);
        Triangle third = new Triangle(c, a, point);

        triangleSoup.add(first);
        triangleSoup.add(second);
        triangleSoup.add(third);

        legalizeEdge(first, new Edge(a, b), point);
        legalizeEdge(second, new Edge(b, c), point);
        legalizeEdge(third, new Edge(c, a), point);
    }

    /**
     * If no containing triangle exists, then the vertex is not
     * inside a triangle (this can also happen due to numerical
     * errors) and lies on an edge. In order to find this edge we
     * search all edges of the triangle soup and select the one
     * which is nearest to the point we try to add. This edge is
     * removed and four new edges are added.
     */
    private void pointOnEdge(Point point) {
        Edge edge = triangleSoup.findNearestEdge(point);

        Triangle first = triangleSoup.findOneTriangleSharing(edge);
        Triangle second = triangleSoup.findNeighbour(first, edge);

        Point firstNoneEdgeVertex = first.getNoneEdgeVertex(edge);
        Point secondNoneEdgeVertex = second.getNoneEdgeVertex(edge);

        triangleSoup.remove(first);
        triangleSoup.remove(second);

        Triangle triangle1 = new Triangle(edge.a, firstNoneEdgeVertex, point);
        Triangle triangle2 = new Triangle(edge.b, firstNoneEdgeVertex, point);
        Triangle triangle3 = new Triangle(edge.a, secondNoneEdgeVertex, point);
        Triangle triangle4 = new Triangle(edge.b, secondNoneEdgeVertex, point);

        triangleSoup.add(triangle1);
        triangleSoup.add(triangle2);
        triangleSoup.add(triangle3);
        triangleSoup.add(triangle4);

        legalizeEdge(triangle1, new Edge(edge.a, firstNoneEdgeVertex), point);
        legalizeEdge(triangle2, new Edge(edge.b, firstNoneEdgeVertex), point);
        legalizeEdge(triangle3, new Edge(edge.a, secondNoneEdgeVertex), point);
        legalizeEdge(triangle4, new Edge(edge.b, secondNoneEdgeVertex), point);
    }

    /**
     * In order for the in circumcircle test to not consider the vertices of
     * the super triangle we have to start out with a big triangle
     * containing the whole point set. We have to scale the super triangle
     * to be very large. Otherwise the triangulation is not convex.
     */
    private Triangle generateSuperTriangle() {
        double maxOfAnyCoordinate = 0.0d;

        for (Point point : getPointSet()) {
            maxOfAnyCoordinate = Math.max(Math.max(point.getX(), point.getY()), maxOfAnyCoordinate);
        }

        maxOfAnyCoordinate *= 16.0d;

        Point p1 = new Point(0.0d, 3.0d * maxOfAnyCoordinate);
        Point p2 = new Point(3.0d * maxOfAnyCoordinate, 0.0d);
        Point p3 = new Point(-3.0d * maxOfAnyCoordinate, -3.0d * maxOfAnyCoordinate);
        return new Triangle(p1, p2, p3);
    }

    public void addPoint(Point point) {
        if (pointSet.contains(point)) {
            return;
        }
        pointSet.add(point);
        if (pointSet.size() < 3) {
            throw new IllegalArgumentException("Less than three points in point set.");
        }
        if (pointSet.size() == 3) {
            triangulate();
            return;
        }
        insertOnePoint(point);
    }

    public void removePoint(Point point) {
        if (!pointSet.contains(point)) {
            throw new IllegalArgumentException("The point " + point.toString() + " not in delaunay point set.");
        }

        List<Triangle> trianglesToBeRemoved = triangleSoup.findVertexTriangles(point);
        Point unitedPoint = getUnitedPoint(trianglesToBeRemoved, point);
        List<Triangle> newTriangles = getNewTriangles(trianglesToBeRemoved, point, unitedPoint);
        triangleSoup.removeAll(trianglesToBeRemoved);
        triangleSoup.addAll(newTriangles);
        for (Triangle newTriangle : newTriangles) {
            if (triangleSoup.contains(newTriangle)) {
                if (newTriangle.a == unitedPoint) {
                    legalizeEdge(newTriangle, new Edge(newTriangle.a, newTriangle.b), newTriangle.c);
                    legalizeEdge(newTriangle, new Edge(newTriangle.a, newTriangle.c), newTriangle.b);
                }
                if (newTriangle.b == unitedPoint) {
                    legalizeEdge(newTriangle, new Edge(newTriangle.a, newTriangle.b), newTriangle.c);
                    legalizeEdge(newTriangle, new Edge(newTriangle.b, newTriangle.c), newTriangle.a);
                }
                if (newTriangle.c == unitedPoint) {
                    legalizeEdge(newTriangle, new Edge(newTriangle.b, newTriangle.c), newTriangle.a);
                    legalizeEdge(newTriangle, new Edge(newTriangle.a, newTriangle.c), newTriangle.b);
                }
            }
        }
    }

    private List<Triangle> getNewTriangles(List<Triangle> trianglesToBeRemoved, Point point, Point unitedPoint) {
        List<Triangle> newTriangles = new ArrayList<>();
        for (Triangle triangle : trianglesToBeRemoved) {
            if (triangle.hasVertex(unitedPoint)) {
                continue;
            }
            Triangle unitedTriangle = getUnitedTriangle(triangle, unitedPoint, point);
            newTriangles.add(unitedTriangle);
        }
        return newTriangles;
    }

    private Triangle getUnitedTriangle(Triangle triangle, Point unitedPoint, Point point) {
        if (triangle.a == point) {
            return new Triangle(unitedPoint, triangle.b, triangle.c);
        }
        if (triangle.b == point) {
            return new Triangle(triangle.a, unitedPoint, triangle.c);
        }
        return new Triangle(triangle.a, triangle.b, unitedPoint);
    }

    private Point getUnitedPoint(List<Triangle> trianglesToBeRemoved, Point point) {
        Map<Triangle, Double> triangleAngleMap = trianglesToBeRemoved.stream().
                collect(Collectors.toMap(Function.identity(), triangle -> getVertexAngle(triangle, point)));
        Map<Point, Double> pointAngleSumMap = new HashMap<>();
        for (Triangle triangle : trianglesToBeRemoved) {
            Double vertexAngle = triangleAngleMap.get(triangle);
            deal(triangle.a, point, vertexAngle, pointAngleSumMap);
            deal(triangle.b, point, vertexAngle, pointAngleSumMap);
            deal(triangle.c, point, vertexAngle, pointAngleSumMap);
        }
        Point unitedPoint = pointAngleSumMap.entrySet().stream().
                reduce((o1, o2) -> o1.getValue() < o2.getValue() ? o1 : o2).get().getKey();
        return unitedPoint;
    }

    private void deal(Point vertex, Point point, Double vertexAngle, Map<Point, Double> pointAngleSumMap) {
        if (vertex != point) {
            double angle = pointAngleSumMap.computeIfAbsent(vertex, k -> 0.0);
            pointAngleSumMap.put(vertex, angle + vertexAngle);
        }
    }

    private double getVertexAngle(Triangle triangle, Point point) {
        if (triangle.a == point) {
            return new Vector(point, triangle.b).angle(new Vector(point, triangle.c));
        }
        if (triangle.b == point) {
            return new Vector(point, triangle.a).angle(new Vector(point, triangle.c));
        }
        return new Vector(point, triangle.a).angle(new Vector(point, triangle.b));
    }

    /**
     * This method legalizes edges by recursively flipping all illegal edges.
     *
     * @param triangle  The triangle
     * @param edge      The edge to be legalized
     * @param newVertex The new vertex
     */
    private void legalizeEdge(Triangle triangle, Edge edge, Point newVertex) {
        Triangle neighbourTriangle = triangleSoup.findNeighbour(triangle, edge);
        // If the triangle has a neighbor, then legalize the edge
        if (neighbourTriangle != null) {
            if (neighbourTriangle.isPointInCircumcircle(newVertex)) {
                triangleSoup.remove(triangle);
                triangleSoup.remove(neighbourTriangle);

                Point noneEdgeVertex = neighbourTriangle.getNoneEdgeVertex(edge);

                Triangle firstTriangle = new Triangle(noneEdgeVertex, edge.a, newVertex);
                Triangle secondTriangle = new Triangle(noneEdgeVertex, edge.b, newVertex);

                triangleSoup.add(firstTriangle);
                triangleSoup.add(secondTriangle);

                legalizeEdge(firstTriangle, new Edge(noneEdgeVertex, edge.a), newVertex);
                legalizeEdge(secondTriangle, new Edge(noneEdgeVertex, edge.b), newVertex);
            }
        }
    }

    /**
     * Returns the point set in form of a vector of 2D vectors.
     *
     * @return Returns the points set.
     */
    public Set<Point> getPointSet() {
        return pointSet;
    }

    /**
     * Returns the triangles of the triangulation in form of a vector of 2D
     * triangles.
     *
     * @return Returns the triangles of the triangulation.
     */
    public List<Triangle> getTriangles() {
        TriangleSoup triangleSoup1 = new TriangleSoup(triangleSoup);
        // Remove all triangles that contain vertices of the super triangle.
        if (superTriangle != null) {
            triangleSoup1.removeTrianglesUsing(superTriangle.a);
            triangleSoup1.removeTrianglesUsing(superTriangle.b);
            triangleSoup1.removeTrianglesUsing(superTriangle.c);
        }
        return triangleSoup1.getTriangles();
    }
}
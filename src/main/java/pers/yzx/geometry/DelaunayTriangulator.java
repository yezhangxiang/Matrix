package pers.yzx.geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of incremental Delaunay triangulation algorithm.only for 2D.
 */
public class DelaunayTriangulator {
    private final Set<Point> pointSet;
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
        Point a = triangle.getA();
        Point b = triangle.getB();
        Point c = triangle.getC();

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

        Triangle triangle1 = new Triangle(edge.getA(), firstNoneEdgeVertex, point);
        Triangle triangle2 = new Triangle(edge.getB(), firstNoneEdgeVertex, point);
        Triangle triangle3 = new Triangle(edge.getA(), secondNoneEdgeVertex, point);
        Triangle triangle4 = new Triangle(edge.getB(), secondNoneEdgeVertex, point);

        triangleSoup.add(triangle1);
        triangleSoup.add(triangle2);
        triangleSoup.add(triangle3);
        triangleSoup.add(triangle4);

        legalizeEdge(triangle1, new Edge(edge.getA(), firstNoneEdgeVertex), point);
        legalizeEdge(triangle2, new Edge(edge.getB(), firstNoneEdgeVertex), point);
        legalizeEdge(triangle3, new Edge(edge.getA(), secondNoneEdgeVertex), point);
        legalizeEdge(triangle4, new Edge(edge.getB(), secondNoneEdgeVertex), point);
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
            maxOfAnyCoordinate = Math.max(Math.max(Math.abs(point.getX()), Math.abs(point.getY())), maxOfAnyCoordinate);
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
        Polygon polygon = getEnvelope(trianglesToBeRemoved, point);
        DelaunayTriangulator localTriangulator = new DelaunayTriangulator(new HashSet<>(polygon.getPoints()));
        localTriangulator.triangulate();
        List<Triangle> localTriangles = localTriangulator.getTriangles();
        List<Triangle> polygonTriangles = localTriangles.stream().
                filter(triangle -> polygon.isInside(triangle.getCentroid())).collect(Collectors.toList());
        pointSet.remove(point);
        triangleSoup.removeAll(trianglesToBeRemoved);
        triangleSoup.addAll(polygonTriangles);
        List<Edge> envelopeEdges = getEdges(polygon);
        for (Triangle polygonTriangle : polygonTriangles) {
            for (Edge envelopeEdge : envelopeEdges) {
                if (polygonTriangle.isNeighbour(envelopeEdge)) {
                    legalizeEdge(polygonTriangle, envelopeEdge, polygonTriangle.getNoneEdgeVertex(envelopeEdge));
                }
            }
        }
    }

    private List<Edge> getEdges(Polygon polygon) {
        List<Point> points = polygon.getPoints();
        int pointSize = points.size();
        List<Edge> edges = new ArrayList<>(pointSize);
        for (int i = 0; i < pointSize; i++) {
            Point point1 = points.get(i);
            Point point2 = points.get((i + 1) % pointSize);
            edges.add(new Edge(point1, point2));
        }
        return edges;
    }

    private Polygon getEnvelope(List<Triangle> triangles, Point point) {
        List<Point> polygonPoints = new ArrayList<>(triangles.size());
        if (triangles.isEmpty()) {
            return new Polygon(polygonPoints);
        }
        Triangle firstTriangle = triangles.get(0);
        Point lastPoint = startUp(firstTriangle, point, polygonPoints);
        Set<Triangle> pastedTriangles = new HashSet<>(triangles.size());
        pastedTriangles.add(firstTriangle);
        for (int i = 1; i < triangles.size(); i++) {
            for (Triangle triangle : triangles) {
                if (pastedTriangles.contains(triangle)) {
                    continue;
                }
                if (triangle.hasVertex(lastPoint)) {
                    Set<Point> pointSet = triangle.getPointSet();
                    pointSet.remove(point);
                    pointSet.remove(lastPoint);
                    Point next = pointSet.iterator().next();
                    polygonPoints.add(next);
                    lastPoint = next;
                    pastedTriangles.add(triangle);
                    break;
                }
            }
        }
        return new Polygon(polygonPoints);
    }

    private Point startUp(Triangle firstTri, Point point, List<Point> points) {
        Point firPoint = null;
        Point secondPoint = null;
        if (firstTri.getA() == point) {
            firPoint = firstTri.getB();
            secondPoint = firstTri.getC();
        }
        if (firstTri.getB() == point) {
            firPoint = firstTri.getC();
            secondPoint = firstTri.getA();
        }
        if (firstTri.getC() == point) {
            firPoint = firstTri.getA();
            secondPoint = firstTri.getB();
        }
        points.add(firPoint);
        points.add(secondPoint);
        return secondPoint;
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

                Triangle firstTriangle = new Triangle(noneEdgeVertex, edge.getA(), newVertex);
                Triangle secondTriangle = new Triangle(noneEdgeVertex, edge.getB(), newVertex);

                triangleSoup.add(firstTriangle);
                triangleSoup.add(secondTriangle);

                legalizeEdge(firstTriangle, new Edge(noneEdgeVertex, edge.getA()), newVertex);
                legalizeEdge(secondTriangle, new Edge(noneEdgeVertex, edge.getB()), newVertex);
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
            triangleSoup1.removeTrianglesUsing(superTriangle.getA());
            triangleSoup1.removeTrianglesUsing(superTriangle.getB());
            triangleSoup1.removeTrianglesUsing(superTriangle.getC());
        }
        return triangleSoup1.getTriangles();
    }
}
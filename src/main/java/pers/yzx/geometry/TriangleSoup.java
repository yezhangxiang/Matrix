package pers.yzx.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Triangle soup class implementation.
 *
 * @author Johannes Diemke
 */
class TriangleSoup {

    private final List<Triangle> triangleSoup;

    TriangleSoup(TriangleSoup triangleSoup) {
        this.triangleSoup = new ArrayList<>(triangleSoup.triangleSoup);
    }

    /**
     * Constructor of the triangle soup class used to create a new triangle soup
     * instance.
     */
    public TriangleSoup() {
        this.triangleSoup = new ArrayList<>();
    }

    /**
     * Adds a triangle to this triangle soup.
     *
     * @param triangle The triangle to be added to this triangle soup
     */
    public void add(Triangle triangle) {
        this.triangleSoup.add(triangle);
    }

    /**
     * Removes a triangle from this triangle soup.
     *
     * @param triangle The triangle to be removed from this triangle soup
     */
    public void remove(Triangle triangle) {
        this.triangleSoup.remove(triangle);
    }

    /**
     * Returns the triangles from this triangle soup.
     *
     * @return The triangles from this triangle soup
     */
    public List<Triangle> getTriangles() {
        return this.triangleSoup;
    }

    /**
     * Returns the triangle from this triangle soup that contains the specified
     * point or null if no triangle from the triangle soup contains the point.
     *
     * @param point The point
     * @return Returns the triangle from this triangle soup that contains the
     * specified point or null
     */
    public Triangle findContainingTriangle(Point point) {
        for (Triangle triangle : triangleSoup) {
            if (triangle.contains(point)) {
                return triangle;
            }
        }
        return null;
    }

    /**
     * Returns the neighbor triangle of the specified triangle sharing the same
     * edge as specified. If no neighbor sharing the same edge exists null is
     * returned.
     *
     * @param triangle The triangle
     * @param edge     The edge
     * @return The triangles neighbor triangle sharing the same edge or null if
     * no triangle exists
     */
    public Triangle findNeighbour(Triangle triangle, Edge edge) {
        for (Triangle triangleFromSoup : triangleSoup) {
            if (triangleFromSoup.isNeighbour(edge) && triangleFromSoup != triangle) {
                return triangleFromSoup;
            }
        }
        return null;
    }

    /**
     * Returns one of the possible triangles sharing the specified edge. Based
     * on the ordering of the triangles in this triangle soup the returned
     * triangle may differ. To find the other triangle that shares this edge use
     * the {@link findNeighbour( Triangle triangle, Edge edge)} method.
     *
     * @param edge The edge
     * @return Returns one triangle that shares the specified edge
     */
    public Triangle findOneTriangleSharing(Edge edge) {
        for (Triangle triangle : triangleSoup) {
            if (triangle.isNeighbour(edge)) {
                return triangle;
            }
        }
        return null;
    }

    /**
     * Returns the edge from the triangle soup nearest to the specified point.
     *
     * @param point The point
     * @return The edge from the triangle soup nearest to the specified point
     */
    public Edge findNearestEdge(Point point) {
        List<EdgeDistancePack> edgeList = new ArrayList<>();

        for (Triangle triangle : triangleSoup) {
            edgeList.add(triangle.findNearestEdge(point));
        }

        EdgeDistancePack[] edgeDistancePacks = new EdgeDistancePack[edgeList.size()];
        edgeList.toArray(edgeDistancePacks);

        Arrays.sort(edgeDistancePacks);
        return edgeDistancePacks[0].edge;
    }

    /**
     * Removes all triangles from this triangle soup that contain the specified
     * vertex.
     *
     * @param vertex The vertex
     */
    public void removeTrianglesUsing(Point vertex) {
        List<Triangle> trianglesToBeRemoved = new ArrayList<>();

        for (Triangle triangle : triangleSoup) {
            if (triangle.hasVertex(vertex)) {
                trianglesToBeRemoved.add(triangle);
            }
        }

        triangleSoup.removeAll(trianglesToBeRemoved);
    }

    public List<Triangle> findVertexTriangles(Point point) {
        return triangleSoup.stream().filter(triangle -> triangle.hasVertex(point)).collect(Collectors.toList());
    }

    public void removeAll(List<Triangle> trianglesToBeRemoved) {
        triangleSoup.removeAll(trianglesToBeRemoved);
    }

    public void addAll(List<Triangle> newTriangles) {
        triangleSoup.addAll(newTriangles);
    }

    public boolean contains(Triangle newTriangle) {
        return triangleSoup.contains(newTriangle);
    }
}
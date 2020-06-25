package pers.yzx.geometry;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DelaunayTriangulatorTest {
    @Test
    public void triangulate() throws Exception {
        double step = 200;
        List<Point> points = new ArrayList<>();
        for (double x = 0; x < 10000; x += step) {
            for (int y = 0; y < 10000; y += step) {
                points.add(new Point(x, y));
            }
        }
        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(points);
//        delaunayTriangulator.shuffle();
        delaunayTriangulator.triangulate();
        List<Triangle> triangles = delaunayTriangulator.getTriangles();
        assertEquals(4802, triangles.size());
    }
}
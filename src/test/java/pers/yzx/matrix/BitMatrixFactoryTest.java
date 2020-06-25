package pers.yzx.matrix;

import org.junit.Test;
import pers.yzx.geometry.Point;
import pers.yzx.geometry.Polygon;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BitMatrixFactoryTest {
    @Test
    public void createByScanLine() throws Exception {
        List<Point> points1 = new ArrayList<>();
        points1.add(new Point(0, 0));
        points1.add(new Point(6, 0));
        points1.add(new Point(3, 3));
        points1.add(new Point(0, 6));
        Polygon polygon1 = new Polygon(points1);
        BitMatrix bitMatrix1 = BitMatrixFactory.createByScanLine(polygon1, 1);
        bitMatrix1.print();
        assertEquals(15, bitMatrix1.getTrueElementsCount());

        List<Point> points2 = new ArrayList<>();
        points2.add(new Point(0, 0));
        points2.add(new Point(6, 0));
        points2.add(new Point(0, 6));
        Polygon polygon2 = new Polygon(points2);
        BitMatrix bitMatrix2 = BitMatrixFactory.createByScanLine(polygon2, 1);
        bitMatrix2.print();
        assertEquals(15, bitMatrix2.getTrueElementsCount());

        List<Point> points3 = new ArrayList<>();
        points3.add(new Point(0, 0));
        points3.add(new Point(6, 0));
        points3.add(new Point(6, 6));
        points3.add(new Point(3, 3));
        points3.add(new Point(0, 6));
        Polygon polygon3 = new Polygon(points3);
        BitMatrix bitMatrix3 = BitMatrixFactory.createByScanLine(polygon3, 1);
        bitMatrix3.print();
        assertEquals(24, bitMatrix3.getTrueElementsCount());

        List<Point> points4 = new ArrayList<>();
        points4.add(new Point(4, 0));
        points4.add(new Point(6, 2));
        points4.add(new Point(8, 2));
        points4.add(new Point(6, 4));
        points4.add(new Point(4, 2));
        points4.add(new Point(2, 4));
        points4.add(new Point(0, 2));
        points4.add(new Point(2, 2));
        Polygon polygon4 = new Polygon(points4);
        BitMatrix bitMatrix4 = BitMatrixFactory.createByScanLine(polygon4, 1);
        bitMatrix4.print();
        assertEquals(10, bitMatrix4.getTrueElementsCount());
    }
}
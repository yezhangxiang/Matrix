package pers.yzx.geometry;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PolygonTest {

    @Test
    public void isInside() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(55, 0));
        points.add(new Point(50, -5));
        points.add(new Point(55, -10));
        points.add(new Point(60, -5));
        points.add(new Point(70, -10));
        points.add(new Point(75, 0));
        points.add(new Point(70, 5));
        points.add(new Point(60, 5));
        points.add(new Point(65, 10));
        points.add(new Point(50, 15));
        Polygon polygon = new Polygon(points);
        Assert.assertTrue(polygon.isInside(new Point(60, 1)));
        Assert.assertTrue(polygon.isInside(new Point(55, 9)));
        Assert.assertFalse(polygon.isInside(new Point(60, -8)));
        Assert.assertFalse(polygon.isInside(new Point(52.6941426782373,2.1216045861976)));
        Assert.assertFalse(polygon.isInside(new Point(66, 8)));
        Assert.assertFalse(polygon.isInside(new Point(62.8644317621048,13.0883620922958)));
        Assert.assertTrue(polygon.isInside(new Point(56.8602851945204,5.0011442665698)));
        Assert.assertTrue(polygon.isInside(new Point(65, 5)));
        Assert.assertTrue(polygon.isInside(new Point(54.8384807380889,-7.98741769596)));
        Assert.assertTrue(polygon.isInside(new Point(67.8883095023285,-4.9853444121677)));
        Assert.assertTrue(polygon.isInside(new Point(69.91011395876,-1.9832711283755)));
        Assert.assertFalse(polygon.isInside(new Point(72,-8)));
        Assert.assertFalse(polygon.isInside(new Point(50.7960137969756,-8.0288719263278)));
    }
}
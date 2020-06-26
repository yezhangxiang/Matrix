package pers.yzx.geometry;

import pers.yzx.matrix.Bound;

import java.util.List;

public class Polygon {
    private final List<Point> points;

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Bound getBound(int resolution) {
        double top = -Double.MAX_VALUE;
        double left = Double.MAX_VALUE;
        double bottom = Double.MAX_VALUE;
        double right = -Double.MAX_VALUE;

        for (Point point : points) {
            top = Math.max(top, point.getY());
            left = Math.min(left, point.getX());
            bottom = Math.min(bottom, point.getY());
            right = Math.max(right, point.getX());
        }

        return Bound.adjustBound(left, top, right, bottom, resolution);
    }

    /**
     * test point is inside a polygon by ray crossing
     * 被测试点在多边形边上，或者和顶点y一样的情况有可能结果不正确
     *
     * @param point 被测试点
     * @return true if point in polygon
     */
    public boolean isInside(Point point) {
        int crossTime = 0;
        int pointSize = points.size();
        for (int i = 0; i < pointSize; i++) {
            Point point1 = points.get(i);
            Point point2 = points.get((i + 1) % pointSize);
            if (point1.getY() == point2.getY()) {
                continue;
            }
            if (point.getY() < Math.min(point1.getY(), point2.getY())) {
                continue;
            }
            if (point.getY() > Math.max(point1.getY(), point2.getY())) {
                continue;
            }
            double x = (point.getY() - point1.getY()) * (point2.getX() - point1.getX()) / (point2.getY() - point1.getY()) + point1.getX();
            if (x > point.getX()) {
                ++crossTime;
            }
        }
        return crossTime % 2 == 1;
    }
}

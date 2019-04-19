package com.yzx.matrix;

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
}

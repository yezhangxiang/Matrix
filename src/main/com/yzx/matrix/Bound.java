package com.yzx.matrix;

public class Bound {
    private final double topLeftX;
    private final double topLeftY;
    private final int rowCount;
    private final int columnCount;
    private final int resolution;

    public Bound(double topLeftX, double topLeftY, int rowCount, int columnCount, int resolution) {
        if (rowCount < 0 || columnCount < 0) {
            throw new IllegalArgumentException("Illegal capacity: row " + rowCount +
            ", or column " + columnCount);
        }
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.resolution = resolution;
    }

    public static Bound adjustBound(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY, int resolution) {
        double topLeftXAdjust = Math.floor(topLeftX / resolution) * resolution;
        double topLeftYAdjust = Math.ceil(topLeftY / resolution) * resolution;
        int columnCount = (int) ((bottomRightX - topLeftXAdjust) / resolution) + 1;
        int rowCount = (int) ((topLeftYAdjust - bottomRightY) / resolution) + 1;
        return new Bound(topLeftX, topLeftY, rowCount, columnCount, resolution);
    }

    public double getTopLeftX() {
        return topLeftX;
    }

    public double getTopLeftY() {
        return topLeftY;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getResolution() {
        return resolution;
    }

    public double getBottomRightX() {
        return topLeftX + columnCount * resolution;
    }

    public double getBottomRightY() {
        return topLeftY - rowCount * resolution;
    }

    public final Index getFlatIndex(Point point) {
        if (!isInside(point)) {
            return null;
        }
        int columnIndex = (int) ((point.getX() - topLeftX) / resolution);
        int rowIndex = (int) ((topLeftY - point.getY()) / resolution);
        return new Index(rowIndex, columnIndex);
    }

    public boolean isInside(Point point) {
        return point.getX() < getBottomRightX() && point.getX() >= topLeftX &&
                point.getY() <= topLeftY && point.getY() > getBottomRightY();
    }

    public Point getPoint(Index index) {
        double x = topLeftX + index.getColumnIndex() * resolution;
        double y = topLeftY - index.getRowIndex() * resolution;
        return new Point(x, y);
    }

    public Bound intersect(Bound bound) {
        if (bound.resolution != resolution) {
            throw new UnsupportedOperationException();
        }
        double left = Math.max(topLeftX, bound.topLeftX);
        double right = Math.min(getBottomRightX(), bound.getBottomRightX());
        double bottom = Math.max(getBottomRightY(), bound.getBottomRightY());
        double top = Math.min(topLeftY, bound.topLeftY);
        if (left >= right || bottom >= top) {
            return null;
        }
        int rowCount = (int) ((top - bottom) / resolution);
        int columnCount = (int) ((right - left) / resolution);
        return new Bound(left, top, rowCount, columnCount, resolution);
    }

    public Bound union(Bound bound) {
        if (bound.resolution != resolution) {
            throw new UnsupportedOperationException();
        }
        double left = Math.min(topLeftX, bound.topLeftX);
        double right = Math.max(getBottomRightX(), bound.getBottomRightX());
        double bottom = Math.min(getBottomRightY(), bound.getBottomRightY());
        double top = Math.max(topLeftY, bound.topLeftY);
        int rowCount = (int) ((top - bottom) / resolution);
        int columnCount = (int) ((right - left) / resolution);
        return new Bound(left, top, rowCount, columnCount, resolution);
    }

    public int getCount() {
        return rowCount * columnCount;
    }
}

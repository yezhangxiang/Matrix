package com.yzx.matrix;

/**
 * Created by kele on 2019/4/14.
 */
public class Bound {
    private final double topLeftX;
    private final double topLeftY;
    private final int rowCount;
    private final int columnCount;
    private final int resolution;

    public Bound(double topLeftX, double topLeftY, int rowCount, int columnCount, int resolution) {
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
}

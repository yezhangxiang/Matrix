package com.yzx.matrix;

public abstract class AbstractMatrix<E> implements Matrix<E> {
    protected int rowCount;
    protected int columnCount;
    protected final int resolution;
    protected double topLeftX;
    protected double topLeftY;

    public AbstractMatrix(int rowCount, int columnCount, int resolution, double topLeftX, double topLeftY) {
        this.resolution = resolution;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        if (rowCount < 0 || columnCount < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
            rowCount + ", or " + columnCount);

        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public E get(double x, double y) {
        int columnIndex = (int)((x - topLeftX) / resolution);
        int rowIndex = (int)((topLeftY - y) / resolution);

        if (rowIndex >= rowCount || columnIndex >= columnCount)
            return null;

        return get(rowIndex, columnIndex);
    }

    protected int getGlobalIndex(int rowIndex, int columnIndex) {
        return rowIndex * columnCount + columnIndex;
    }

    @Override
    public void set(int flatIndex, E element) {
        int rowIndex = flatIndex / columnCount;
        int columnIndex = flatIndex % columnCount;
        set(rowIndex, columnIndex, element);
    }

    @Override
    public void set(double x, double y, E element) {
        int columnIndex = (int) ((x - topLeftX) / resolution);
        int rowIndex = (int) ((topLeftY - y) / resolution);
        set(columnIndex, rowIndex, element);
    }

    protected void rangeCheck(int rowIndex, int columnIndex) {
        if (rowIndex >= rowCount || columnIndex >= columnCount)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(rowIndex, columnIndex));
    }

    private String outOfBoundsMsg(int rowIndex, int columnIndex) {
        return "Row index: " + rowIndex + ", row size: " + rowIndex +
                "; Column index: " + columnIndex + ", cloumn size: " + columnCount;
    }

    @Override
    public double getTopLeftX() {
        return topLeftX;
    }

    @Override
    public double getTopLeftY() {
        return topLeftY;
    }

    @Override
    public double getBottomRightX() {
        return topLeftX + columnCount * resolution;
    }

    @Override
    public double getBottomRightY() {
        return topLeftY - rowCount * resolution;
    }

    @Override
    public int getResolution() {
        return resolution;
    }

    @Override
    public int getStartRow(Matrix matrix) {
        return (int)(topLeftY - matrix.getTopLeftY()) / resolution;
    }

    @Override
    public int getStartColumn(Matrix matrix) {
        return (int)(-topLeftX + matrix.getTopLeftX()) / resolution;
    }
}

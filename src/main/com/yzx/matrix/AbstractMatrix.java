package com.yzx.matrix;

import java.util.Iterator;

public abstract class AbstractMatrix<E> implements Matrix<E> {
    int rowCount;
    int columnCount;
    final int resolution;
    double topLeftX;
    double topLeftY;

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

    public AbstractMatrix(Bound bound) {
        this.resolution = bound.getResolution();
        this.rowCount = bound.getRowCount();
        this.columnCount = bound.getColumnCount();
        this.topLeftX = bound.getTopLeftX();
        this.topLeftY = bound.getTopLeftY();
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
    public int getCount() {
        return rowCount * columnCount;
    }

    @Override
    public E get(Index index) {
        return get(index.getColumnIndex(), index.getRowIndex());
    }

    @Override
    public void set(int flatIndex, E element) {
        int rowIndex = flatIndex / columnCount;
        int columnIndex = flatIndex % columnCount;
        set(rowIndex, columnIndex, element);
    }
    @Override
    public void set(Index index, E element) {
        set(index.getRowIndex(), index.getColumnIndex(), element);
    }

    protected void rangeCheck(int rowIndex, int columnIndex) {
        if (rowIndex >= rowCount || columnIndex >= columnCount)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(rowIndex, columnIndex));
    }

    private String outOfBoundsMsg(int rowIndex, int columnIndex) {
        return "Row index: " + rowIndex + ", row size: " + rowIndex +
                "; Column index: " + columnIndex + ", column size: " + columnCount;
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
    public boolean isInside(Point point) {
        return point.getX() < getBottomRightX() && point.getX() >= topLeftX &&
                point.getY() <= topLeftY && point.getY() > getBottomRightY();
    }

    @Override
    public int getStartRow(Matrix matrix) {
        return (int)(topLeftY - matrix.getTopLeftY()) / resolution;
    }

    @Override
    public int getStartColumn(Matrix matrix) {
        return (int)(-topLeftX + matrix.getTopLeftX()) / resolution;
    }

    @Override
    public int getFlatIndex(Index index) {
        return index.getRowIndex() * columnCount + index.getColumnIndex();
    }

    @Override
    public int getFlatIndex(int rowIndex, int columnIndex) {
        return rowIndex * columnCount + columnIndex;
    }


    @Override
    public Index getIndex(int flatIndex) {
        if (-1 == flatIndex) {
            return null;
        }
        int rowIndex = flatIndex / columnCount;
        int columnIndex = flatIndex % columnCount;
        return new Index(rowIndex, columnIndex);
    }

    @Override
    public Index getIndex(Matrix<?> matrix, Index matrixIndex) {
        double x = matrix.getTopLeftX() + matrixIndex.getColumnIndex() * matrix.getResolution();
        double y = matrix.getTopLeftY() - matrixIndex.getRowIndex() * matrix.getResolution();
        int rowIndex = (int) ((x - topLeftX) / resolution);
        int columnIndex = (int) ((topLeftY - y) / resolution);
        if (rowIndex < 0 || rowIndex >= rowCount || columnIndex < 0 || columnIndex >= columnCount) {
            return null;
        }
        return new Index(rowIndex, columnIndex);
    }

    @Override
    public Index getIndex(Point point) {
        if (!isInside(point)) {
            return null;
        }
        int rowIndex = (int) ((point.getX() - topLeftX) / resolution);
        int columnIndex = (int) ((topLeftY - point.getY()) / resolution);
        return new Index(rowIndex, columnIndex);
    }

    @Override
    public Point getPoint(Index index) {
        double x = topLeftX + index.getColumnIndex() * resolution;
        double y = topLeftY - index.getRowIndex() * resolution;
        return new Point(x, y);
    }

    @Override
    public Bound getBound() {
        return new Bound(topLeftX, topLeftY, rowCount, columnCount, resolution);
    }

    class arrayIterator implements Iterator<E> {
        private Iterator<Integer> keyIterator = new arrayKeyIterator();
        @Override
        public boolean hasNext() {
            return keyIterator.hasNext();
        }

        @Override
        public E next() {
            return get(keyIterator.next());
        }
    }

    class arrayKeyIterator implements Iterator<Integer> {
        private int currentRowIndex;
        private int currentColumnIndex;
        @Override
        public boolean hasNext() {
            for (; currentRowIndex < rowCount; currentRowIndex++) {
                for (; currentColumnIndex < columnCount; currentColumnIndex++) {
                    if (null != get(currentRowIndex, currentColumnIndex)) {
                        return true;
                    }
                }
                currentColumnIndex = 0;
            }
            return false;
        }

        @Override
        public Integer next() {
            Object o;
            int index;
            do {
                o = get(currentRowIndex, currentColumnIndex);
                index = currentRowIndex * columnCount + currentColumnIndex;
                if (currentColumnIndex == columnCount - 1) {
                    currentRowIndex++;
                    currentColumnIndex = 0;
                } else {
                    currentColumnIndex++;
                }
            } while (null == o);
            return index;
        }
    }
}


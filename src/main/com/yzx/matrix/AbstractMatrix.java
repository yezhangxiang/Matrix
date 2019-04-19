package com.yzx.matrix;

import java.util.Iterator;

public abstract class AbstractMatrix<E> implements Matrix<E> {
    public static final int invalidIndex = -1;
    Bound bound;

    public AbstractMatrix(Bound bound) {
        this.bound = bound;
    }

    @Override
    public int getRowCount() {
        return bound.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return bound.getColumnCount();
    }

    @Override
    public int getCount() {
        return bound.getCount();
    }

    @Override
    public E get(Index index) {
        return get(index.getColumnIndex(), index.getRowIndex());
    }

    @Override
    public void set(int flatIndex, E element) {
        int columnCount = bound.getColumnCount();
        int rowIndex = flatIndex / columnCount;
        int columnIndex = flatIndex % columnCount;
        set(rowIndex, columnIndex, element);
    }
    @Override
    public void set(Index index, E element) {
        set(index.getRowIndex(), index.getColumnIndex(), element);
    }

    protected void rangeCheck(int rowIndex, int columnIndex) {
        if (rowIndex >= bound.getRowCount() || columnIndex >= bound.getColumnCount())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(rowIndex, columnIndex));
    }

    private String outOfBoundsMsg(int rowIndex, int columnIndex) {
        return "Row index: " + rowIndex + ", row size: " + rowIndex +
                "; Column index: " + columnIndex + ", column size: " + bound.getColumnCount();
    }

    @Override
    public double getTopLeftX() {
        return bound.getTopLeftX();
    }

    @Override
    public double getTopLeftY() {
        return bound.getTopLeftY();
    }

    @Override
    public double getBottomRightX() {
        return bound.getBottomRightX();
    }

    @Override
    public double getBottomRightY() {
        return bound.getBottomRightY();
    }

    @Override
    public int getResolution() {
        return bound.getResolution();
    }

    @Override
    public boolean isInside(Point point) {
        return bound.isInside(point);
    }

    @Override
    public int getStartRow(Matrix matrix) {
        return (int)(bound.getTopLeftY() - matrix.getTopLeftY()) / bound.getResolution();
    }

    @Override
    public int getStartColumn(Matrix matrix) {
        return (int)(-bound.getTopLeftX() + matrix.getTopLeftX()) / bound.getResolution();
    }

    @Override
    public int getFlatIndex(Index index) {
        return index.getRowIndex() * bound.getColumnCount() + index.getColumnIndex();
    }

    @Override
    public int getFlatIndex(int rowIndex, int columnIndex) {
        return rowIndex * bound.getColumnCount() + columnIndex;
    }


    @Override
    public Index getIndex(int flatIndex) {
        if (invalidIndex == flatIndex) {
            return null;
        }
        int rowIndex = flatIndex / bound.getColumnCount();
        int columnIndex = flatIndex % bound.getColumnCount();
        return new Index(rowIndex, columnIndex);
    }

    @Override
    public Index getIndex(Matrix<?> matrix, Index matrixIndex) {
        double x = matrix.getTopLeftX() + matrixIndex.getColumnIndex() * matrix.getResolution();
        double y = matrix.getTopLeftY() - matrixIndex.getRowIndex() * matrix.getResolution();
        int rowIndex = (int) ((x - bound.getTopLeftX()) / bound.getResolution());
        int columnIndex = (int) ((bound.getTopLeftY() - y) / bound.getResolution());
        if (rowIndex < 0 || rowIndex >= bound.getRowCount() || columnIndex < 0 || columnIndex >= bound.getColumnCount()) {
            return null;
        }
        return new Index(rowIndex, columnIndex);
    }

    @Override
    public Index getIndex(Point point) {
        if (!isInside(point)) {
            return null;
        }
        int rowIndex = (int) ((point.getX() - bound.getTopLeftX()) / bound.getResolution());
        int columnIndex = (int) ((bound.getTopLeftY() - point.getY()) / bound.getResolution());
        return new Index(rowIndex, columnIndex);
    }

    @Override
    public Point getPoint(Index index) {
        double x = bound.getTopLeftX() + index.getColumnIndex() * bound.getResolution();
        double y = bound.getTopLeftY() - index.getRowIndex() * bound.getResolution();
        return new Point(x, y);
    }

    @Override
    public Bound getBound() {
        return new Bound(bound.getTopLeftX(), bound.getTopLeftY(), bound.getRowCount(), bound.getColumnCount(), bound.getResolution());
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
            for (; currentRowIndex < bound.getRowCount(); currentRowIndex++) {
                for (; currentColumnIndex < bound.getColumnCount(); currentColumnIndex++) {
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
                index = currentRowIndex * bound.getColumnCount() + currentColumnIndex;
                if (currentColumnIndex == bound.getColumnCount() - 1) {
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


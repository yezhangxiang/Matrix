package com.yzx.matrix;

import com.yzx.geometry.Point;

import java.util.Iterator;

public abstract class AbstractMatrix<E> implements Matrix<E> {
    public static final int INVALID_FLAT_INDEX = -1;
    private static double floorInterval = 3;
    private static int  maxFloor = 10;
    protected static final int DEFAULT_FLOOR_INDEX = 0;
    int floorCount;
    Bound bound;

    AbstractMatrix(Bound bound) {
        this.bound = bound;
        this.floorCount = 1;
    }

    public AbstractMatrix(Bound bound, int floorCount) {
        if (floorCount < 0)
            throw new IllegalArgumentException("Illegal Capacity: floor " + floorCount);
        this.bound = bound;
        this.floorCount = floorCount;
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
        return bound.getCount() * floorCount;
    }

    @Override
    public E get(int flatIndex) {
        rangeCheck(flatIndex);
        return get(getIndex(flatIndex));
    }

    @Override
    public E get(int rowIndex, int columnIndex) {
        return get(DEFAULT_FLOOR_INDEX, rowIndex, columnIndex);
    }

    @Override
    public E get(Index index) {
        return get(index.getFloorIndex(), index.getRowIndex(), index.getColumnIndex());
    }

    @Override
    public void set(int flatIndex, E element) {
        Index index = getIndex(flatIndex);
        set(index, element);
    }

    @Override
    public void set(int rowIndex, int columnIndex, E element) {
        set(DEFAULT_FLOOR_INDEX, rowIndex, columnIndex, element);
    }


    @Override
    public void set(Index index, E element) {
        set(index.getFloorIndex(), index.getRowIndex(), index.getColumnIndex(), element);
    }

    protected void rangeCheck(int floorIndex, int rowIndex, int columnIndex) {
        if (floorIndex > maxFloor || rowIndex >= bound.getRowCount() || columnIndex >= bound.getColumnCount() ||
                floorIndex < 0 || rowIndex < 0 || columnIndex < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(rowIndex, columnIndex));
    }

    void rangeCheck(int flatIndex) {
        if (flatIndex < 0 || flatIndex > getCount()) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(flatIndex));
        }
    }

    private String outOfBoundsMsg(int flatIndex) {
        return "Flat index: " + flatIndex + ", matrix size: " + getCount();
    }

    private String outOfBoundsMsg(int rowIndex, int columnIndex) {
        return "Row index: " + rowIndex + ", row size: " + getRowCount() +
                "; Column index: " + columnIndex + ", column size: " + getColumnCount();
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
    public int getFloorCount() {
        return floorCount;
    }

    @Override
    public int getResolution() {
        return bound.getResolution();
    }

    @Override
    public boolean isInside(Point point) {
        return point.getZ() >= 0 && point.getZ() < getFloorCount() * floorInterval && bound.isInside(point);
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
        return index.getFloorIndex() * bound.getCount() + index.getRowIndex() * bound.getColumnCount() + index.getColumnIndex();
    }

    @Override
    public int getFlatIndex(int rowIndex, int columnIndex) {
        return rowIndex * bound.getColumnCount() + columnIndex;
    }

    @Override
    public int getFlatIndex(int floorIndex, int rowIndex, int columnIndex) {
        return floorIndex * bound.getCount() + rowIndex * bound.getColumnCount() + columnIndex;
    }


    @Override
    public Index getIndex(int flatIndex) {
        if (flatIndex < 0 || flatIndex >= getCount()) {
            return null;
        }
        int floorIndex = flatIndex / bound.getCount();
        int floorFlatIndex = flatIndex % bound.getCount();
        int rowIndex = floorFlatIndex / bound.getColumnCount();
        int columnIndex = floorFlatIndex % bound.getColumnCount();
        return new Index(floorIndex, rowIndex, columnIndex);
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
        return new Index(matrixIndex.getFloorIndex(), rowIndex, columnIndex);
    }

    @Override
    public Index getIndex(Point point) {
        if (!isInside(point)) {
            return null;
        }
        int floorIndex = (int) (point.getZ() / floorInterval);
        int rowIndex = (int) ((point.getX() - bound.getTopLeftX()) / bound.getResolution());
        int columnIndex = (int) ((bound.getTopLeftY() - point.getY()) / bound.getResolution());
        return new Index(floorIndex, rowIndex, columnIndex);
    }

    @Override
    public Point getPoint(Index index) {
        double x = bound.getTopLeftX() + index.getColumnIndex() * bound.getResolution();
        double y = bound.getTopLeftY() - index.getRowIndex() * bound.getResolution();
        double z = index.getFloorIndex() * floorInterval;
        return new Point(x, y, z);
    }

    @Override
    public Bound getBound() {
        return bound;
    }

    class KeyIterator implements Iterator<Integer> {
        private Iterator<Index> indexItr = new IndexItr();
        @Override
        public boolean hasNext() {
            return indexItr.hasNext();
        }

        @Override
        public Integer next() {
            return getFlatIndex(indexItr.next());
        }

        @Override
        public void remove() {
            indexItr.remove();
        }
    }

    class MatrixIterator implements Iterator<E> {
        private Iterator<Index> indexItr = new IndexItr();
        @Override
        public boolean hasNext() {
            return indexItr.hasNext();
        }

        @Override
        public E next() {
            return get(indexItr.next());
        }

        @Override
        public void remove() {
            indexItr.remove();
        }
    }

    class IndexItr implements Iterator<Index> {
        int floorCursor = 0;
        private int rowCursor = 0;
        private int columnCursor = 0;
        int lastFloorRet = -1;
        int lastRowRet = -1;
        int lastColumnRet = -1;
        @Override
        public boolean hasNext() {
            for (; floorCursor < floorCount; floorCursor++) {
                for (; rowCursor < bound.getRowCount(); rowCursor++) {
                    for (; columnCursor < bound.getColumnCount(); columnCursor++) {
                        if (null != get(floorCursor, rowCursor, columnCursor)) {
                            return true;
                        }
                    }
                    columnCursor = 0;
                }
                rowCursor = 0;
            }
            return false;
        }

        @Override
        public Index next() {
            Object o;
            int floorIndex;
            int rowIndex;
            int columnIndex;
            do {
                floorIndex = floorCursor;
                rowIndex = rowCursor;
                columnIndex = columnCursor;

                o = get(rowCursor, columnCursor);
                if (columnCursor == bound.getColumnCount() - 1) {
                    if (rowCursor == bound.getRowCount() - 1) {
                        floorCursor++;
                        rowCursor = 0;
                    } else {
                        rowCursor++;
                    }
                    columnCursor = 0;
                } else {
                    columnCursor++;
                }
            } while (null == o);
            lastFloorRet = floorIndex;
            lastRowRet = rowIndex;
            lastColumnRet = columnIndex;
            return new Index(floorIndex, rowIndex, columnIndex);
        }

        @Override
        public void remove() {
            if (lastFloorRet < 0 || lastRowRet < 0 || lastColumnRet < 0) {
                throw new IllegalStateException();
            }
            set(lastFloorRet, lastRowRet, lastColumnRet, null);
            lastFloorRet = -1;
            lastRowRet = -1;
            lastColumnRet = -1;
        }
    }
}


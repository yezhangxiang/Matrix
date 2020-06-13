package com.yzx.matrix;

import java.util.Iterator;

public class ArrayMatrix<E> extends AbstractMatrix<E> implements Matrix<E> {
    private Object[][][] elementData;
    private int effectiveCount;

    public ArrayMatrix(Bound bound) {
        super(bound);
        this.elementData = new Object[floorCount][bound.getRowCount()][bound.getColumnCount()];
        this.effectiveCount = 0;
    }

    public ArrayMatrix(Bound bound, int floorCount) {
        super(bound);
        this.elementData = new Object[floorCount][bound.getRowCount()][bound.getColumnCount()];
        this.effectiveCount = 0;
    }

    @Override
    public int getEffectiveCount() {
        return effectiveCount;
    }

    @Override
    public E get(int floorIndex, int rowIndex, int columnIndex) {
        rangeCheck(floorIndex, rowIndex, columnIndex);
        return elementData(floorIndex, rowIndex, columnIndex);
    }

    private E elementData(int floorIndex, int rowIndex, int columnIndex) {
        return (E) elementData[floorIndex][rowIndex][columnIndex];
    }

    @Override
    public void set(int floorIndex, int rowIndex, int columnIndex, E element) {
        rangeCheck(floorIndex, rowIndex, columnIndex);
        if (null == elementData[floorIndex][rowIndex][columnIndex]) {
            effectiveCount++;
        }
        elementData[floorIndex][rowIndex][columnIndex] = element;
    }

    @Override
    public void clear() {
        for (int f = 0; f < floorCount; f++) {
            for (int i = 0; i < bound.getRowCount(); i++) {
                for (int j = 0; j < bound.getColumnCount(); j++) {
                    elementData[f][i][j] = null;
                }
            }
        }
        bound = new Bound(0, 0, 0, 0, 0);
        effectiveCount = 0;
    }

    @Override
    public boolean isEmpty() {
        return effectiveCount == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new MatrixIterator();
    }

    @Override
    public Iterator<Integer> keyIterator() {
        return new KeyIterator();
    }

    @Override
    public Iterator<Index> indexIterator() {
        return new IndexItr();
    }

    @Override
    public Matrix<E> getFloorMatrix(int floorIndex) {
        return null;
    }

}

package com.yzx.matrix;

import java.util.Iterator;

public class ArrayMatrix<E> extends AbstractMatrix<E> implements Matrix<E> {
    private Object[][] elementData;
    private int effectiveCount;

    public ArrayMatrix(Bound bound) {
        super(bound);
        this.elementData = new Object[bound.getRowCount()][bound.getColumnCount()];
        this.effectiveCount = 0;
    }


    @Override
    public int getEffectiveCount() {
        return effectiveCount;
    }

    @Override
    public E get(int rowIndex, int columnIndex) {
        rangeCheck(rowIndex, columnIndex);
        return elementData(rowIndex, columnIndex);
    }

    private E elementData(int rowIndex, int columnIndex) {
        return (E) elementData[rowIndex][columnIndex];
    }


    @Override
    public E get(int flatIndex) {
        int rowIndex = flatIndex / bound.getColumnCount();
        int columnIndex = flatIndex % bound.getColumnCount();
        return get(rowIndex, columnIndex);
    }

    @Override
    public void set(int rowIndex, int columnIndex, E element) {
        rangeCheck(rowIndex, columnIndex);
        if (null == elementData[rowIndex][columnIndex]) {
            effectiveCount++;
        }
        elementData[rowIndex][columnIndex] = element;
    }

    @Override
    public void clear() {
        for (int i = 0; i < bound.getRowCount(); i++) {
            for (int j = 0; j < bound.getColumnCount(); j++) {
                elementData[i][j] = null;
            }
        }
        bound = new Bound(0, 0, 0, 0, 0);
        effectiveCount = 0;
    }

    @Override
    public boolean isEmpty() {
        for (Object[] elementDatum : elementData) {
            for (Object o : elementDatum) {
                if (null != o) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Object clone() {
        ArrayMatrix arrayMatrix = new ArrayMatrix(bound);
        for (int i = 0; i < bound.getRowCount(); i++) {
            for (int j = 0; j < bound.getColumnCount(); j++) {
                if (null != elementData[i][j]) {
                    arrayMatrix.set(i, j, elementData[i][j]);
                }
            }
        }
        return arrayMatrix;
    }

    @Override
    public Iterator<E> iterator() {
        return new arrayIterator();
    }

    @Override
    public Iterator<Integer> keyIterator() {
        return new arrayKeyIterator();
    }

    @Override
    public void crop() {
        int minRow = Integer.MAX_VALUE;
        int minColumn = Integer.MAX_VALUE;
        int maxRow = 0;
        int maxColumn = 0;
        if (0 == effectiveCount) {
            bound = new Bound(0, 0, 0, 0, 0);
        } else {
            for (int rowIndex = 0; rowIndex < bound.getRowCount(); rowIndex++) {
                for (int columnIndex = 0; columnIndex < bound.getColumnCount(); columnIndex++) {
                    if (null == elementData[rowIndex][columnIndex]) {
                        continue;
                    }
                    if (rowIndex < minRow) {
                        minRow = rowIndex;
                    }
                    if (rowIndex > maxRow) {
                        maxRow = rowIndex;
                    }
                    if (columnIndex < minColumn) {
                        minColumn = columnIndex;
                    }
                    if (columnIndex > maxColumn) {
                        maxColumn = columnIndex;
                    }
                }
            }
        }
        int newRowCount = maxRow - minRow + 1;
        int newColumnCount = maxColumn - minColumn + 1;
        Object[][] newElementData = new Object[newRowCount][newColumnCount];
        for (int rowIndex = 0; rowIndex < bound.getRowCount(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < bound.getColumnCount(); columnIndex++) {
                if (null == elementData[rowIndex][columnIndex]) {
                    continue;
                }
                int newRowIndex = rowIndex - minRow;
                int newColumnIndex = columnIndex - minColumn;
                newElementData[newRowIndex][newColumnIndex] = elementData[rowIndex][columnIndex];
            }
        }
        double newTopLeftX = bound.getTopLeftY() + minColumn * bound.getResolution();
        double newTopLeftY = bound.getTopLeftY() - minRow * bound.getResolution();
        bound = new Bound(newTopLeftX, newTopLeftY, newRowCount, newColumnCount, bound.getResolution());
        elementData = newElementData;
    }

}

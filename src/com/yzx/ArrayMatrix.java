package com.yzx;

import java.util.Iterator;

public class ArrayMatrix<E> extends AbstractMatrix<E> implements Matrix<E> {
    protected Object[][] elementData;
    private int effectiveCount;

    public ArrayMatrix(int rowCount, int columnCount, int resolution, double topLeftX, double topLeftY) {
        super(rowCount, columnCount, resolution, topLeftX, topLeftY);
        this.elementData = new Object[rowCount][columnCount];
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
    public E get(int globalIndex) {
        int rowIndex = globalIndex / columnCount;
        int columnIndex = globalIndex % columnCount;
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
        ArrayMatrix arrayMatrix = new ArrayMatrix(rowCount, columnCount, resolution, topLeftX, topLeftY);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
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
            rowCount = 0;
            columnCount = 0;
        } else {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
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
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                if (null == elementData[rowIndex][columnIndex]) {
                    continue;
                }
                int newRowIndex = rowIndex - minRow;
                int newColumnIndex = columnIndex - minColumn;
                newElementData[newRowIndex][newColumnIndex] = elementData[rowIndex][columnIndex];
            }
        }
        topLeftX = topLeftY + minColumn * resolution;
        topLeftY = topLeftY - minRow * resolution;
        rowCount = newRowCount;
        columnCount = newColumnCount;

        elementData = null;
        elementData = newElementData;
    }

    private class arrayIterator implements Iterator<E> {
        private int currentRowIndex;
        private int currentColumnIndex;
        @Override
        public boolean hasNext() {
            return currentRowIndex != rowCount || currentColumnIndex != columnCount;
        }

        @Override
        public E next() {
            E element = elementData(currentRowIndex, currentColumnIndex);
            if (columnCount - 1 == currentColumnIndex) {
                 currentRowIndex++;
                 currentColumnIndex = 0;
            } else {
                currentColumnIndex++;
            }
            return element;
        }
    }

    private class arrayKeyIterator implements Iterator<Integer> {
        private int currnetRowIndex;
        private int currentColumnIndex;
        @Override
        public boolean hasNext() {
            for (; currnetRowIndex < rowCount; currnetRowIndex++) {
                for (; currentColumnIndex < columnCount; currentColumnIndex++) {
                    if (null != elementData[currnetRowIndex][currentColumnIndex]) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Integer next() {
            Object o;
            int index;
            do {
                o = elementData[currnetRowIndex][currentColumnIndex];
                index = currnetRowIndex * columnCount + currentColumnIndex;
                if (currentColumnIndex == columnCount - 1) {
                    currnetRowIndex++;
                    currentColumnIndex = 0;
                } else {
                    currentColumnIndex++;
                }
            } while (null == o);
            return index;
        }
    }
}

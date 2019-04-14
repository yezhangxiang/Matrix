package com.yzx.matrix;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapMatrix<E> extends AbstractMatrix<E> implements Matrix<E> {
    HashMap<Integer, E> elementData;

    public MapMatrix(int rowCount, int columnCount, int resolution, double topLeftX, double topLeftY) {
        super(rowCount, columnCount, resolution, topLeftX, topLeftY);
        this.elementData = new HashMap<>();
    }

    @Override
    public int getEffectiveCount() {
        return elementData.size();
    }

    @Override
    public E get(int rowIndex, int columnIndex) {
        rangeCheck(rowIndex, columnIndex);
        return elementData.get(getGlobalIndex(rowIndex, columnIndex));
    }

    @Override
    public E get(int globalIndex) {
        return elementData.get(globalIndex);
    }

    @Override
    public void set(int rowIndex, int columnIndex, E element) {
        rangeCheck(rowIndex, columnIndex);
        elementData.put(getGlobalIndex(rowIndex, columnIndex), element);
    }

    @Override
    public boolean isEmpty() {
        return elementData.isEmpty();
    }

    public Object clone() {
        MapMatrix<E> mapMatrix = new MapMatrix<>(rowCount, columnCount, resolution, topLeftX, topLeftY);
        for (Map.Entry<Integer, E> integerEEntry : elementData.entrySet()) {
            mapMatrix.set(integerEEntry.getKey(), integerEEntry.getValue());
        }
        return mapMatrix;
    }

    @Override
    public Iterator<E> iterator() {
        return elementData.values().iterator();
    }

    @Override
    public Iterator<Integer> keyIterator() {
        return elementData.keySet().iterator();
    }

    @Override
    public void crop() {
        int minRow = Integer.MAX_VALUE;
        int minColumn = Integer.MAX_VALUE;
        int maxRow = 0;
        int maxColumn = 0;
        if (elementData.isEmpty()) {
            rowCount = 0;
            columnCount = 0;
        } else {
            for (Integer globalIndex : elementData.keySet()) {
                int rowIndex = globalIndex / columnCount;
                int columnIndex = globalIndex % columnCount;
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
        int newColumnCount = maxColumn - minColumn + 1;
        HashMap<Integer, E> newElementData = new HashMap<>();
        for (Integer globalIndex : elementData.keySet()) {
            int rowIndex = globalIndex / columnCount;
            int columnIndex = globalIndex % columnCount;
            int newRowIndex = rowIndex - minRow;
            int newColumnIndex = columnIndex - minColumn;
            newElementData.put(newRowIndex * newColumnCount + newColumnIndex, elementData.get(globalIndex));
        }
        topLeftX = topLeftY + minColumn * resolution;
        topLeftY = topLeftY - minRow * resolution;
        rowCount = maxRow - minRow + 1;
        columnCount = newColumnCount;

        elementData.clear();
        elementData = null;
        elementData = newElementData;
    }
}

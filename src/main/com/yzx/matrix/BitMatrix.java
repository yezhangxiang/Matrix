package com.yzx.matrix;

import java.util.BitSet;
import java.util.Iterator;

public class BitMatrix extends AbstractMatrix<Boolean> {
    private BitSet bitSet;

    public BitMatrix(int rowCount, int columnCount, int resolution, double topLeftX, double topLeftY) {
        super(rowCount, columnCount, resolution, topLeftX, topLeftY);
        bitSet = new BitSet(rowCount*columnCount);
    }

    public BitMatrix(Bound bound) {
        super(bound);
        bitSet = new BitSet(rowCount*columnCount);
    }

    @Override
    public int getEffectiveCount() {
        return rowCount * columnCount;
    }

    @Override
    public Boolean get(int rowIndex, int columnIndex) {
        rangeCheck(rowIndex, columnIndex);
        int flatIndex = rowIndex * columnCount + columnIndex;
        return get(flatIndex);
    }

    @Override
    public Boolean get(int flatIndex) {
        return bitSet.get(flatIndex);
    }

    @Override
    public void set(int rowIndex, int columnIndex, Boolean element) {
        int flatIndex = rowIndex * columnCount + columnIndex;
        bitSet.set(flatIndex, element);
    }

    @Override
    public void clear() {
        rowCount = 0;
        columnCount = 0;
        bitSet.clear();
    }

    @Override
    public boolean isEmpty() {
        return rowCount == 0 && columnCount == 0;
    }

    @Override
    public Iterator<Boolean> iterator() {
        return new arrayIterator();
    }

    @Override
    public Iterator<Integer> keyIterator() {
        return new arrayKeyIterator();
    }

    @Override
    public void crop() {

    }

    public int getTrueElementsCount() {
        return bitSet.cardinality();
    }

    public void print() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                System.out.print(get(i, j) ? "*" : "-");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}

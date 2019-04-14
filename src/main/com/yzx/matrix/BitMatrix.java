package com.yzx.matrix;

import java.util.BitSet;
import java.util.Iterator;

/**
 * Created by kele on 2019/4/14.
 */
public class BitMatrix extends AbstractMatrix<Boolean> {
    private BitSet bitSet;
    private int trueElementsCount;

    public BitMatrix(int rowCount, int columnCount, int resolution, double topLeftX, double topLeftY) {
        super(rowCount, columnCount, resolution, topLeftX, topLeftY);
        bitSet = new BitSet(rowCount*columnCount);
        trueElementsCount = 0;
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
}

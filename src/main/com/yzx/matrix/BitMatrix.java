package com.yzx.matrix;

import java.util.BitSet;
import java.util.Iterator;

public class BitMatrix extends AbstractMatrix<Boolean> {
    private BitSet bitSet;

    public BitMatrix(Bound bound) {
        super(bound);
        bitSet = new BitSet(bound.getRowCount()*bound.getColumnCount());
    }

    @Override
    public int getEffectiveCount() {
        return bound.getRowCount() * bound.getColumnCount();
    }

    @Override
    public Boolean get(int rowIndex, int columnIndex) {
        rangeCheck(rowIndex, columnIndex);
        int flatIndex = rowIndex * bound.getColumnCount() + columnIndex;
        return get(flatIndex);
    }

    @Override
    public Boolean get(int flatIndex) {
        return bitSet.get(flatIndex);
    }

    @Override
    public void set(int rowIndex, int columnIndex, Boolean element) {
        int flatIndex = rowIndex * bound.getColumnCount() + columnIndex;
        bitSet.set(flatIndex, element);
    }

    @Override
    public void clear() {
        bound = new Bound(0, 0, 0, 0, 0);
        bitSet.clear();
    }

    @Override
    public boolean isEmpty() {
        return bound.getRowCount() == 0 && bound.getColumnCount() == 0;
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
        for (int i = 0; i < bound.getRowCount(); i++) {
            for (int j = 0; j < bound.getColumnCount(); j++) {
                System.out.print(get(i, j) ? "*" : "-");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}

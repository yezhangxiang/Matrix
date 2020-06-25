package pers.yzx.matrix;

import java.util.BitSet;
import java.util.Iterator;

public class BitMatrix extends AbstractMatrix<Boolean> {
    private final BitSet bitSet;
    private int trueElementCount;

    public BitMatrix(Bound bound) {
        super(bound);
        bitSet = new BitSet(getCount());
        this.trueElementCount = 0;
    }

    public BitMatrix(Bound bound, int floorCount) {
        super(bound, floorCount);
        bitSet = new BitSet(getCount());
        this.trueElementCount = 0;
    }

    public BitMatrix(Bound bound, boolean defaultValue) {
        super(bound);
        bitSet = new BitSet(getCount());
        this.trueElementCount = 0;
        if (defaultValue) {
            bitSet.set(0, getCount());
            this.trueElementCount = getCount();
        }
    }

    public BitMatrix(Bound bound, int floorCount, boolean defaultValue) {
        super(bound, floorCount);
        bitSet = new BitSet(getCount());
        this.trueElementCount = 0;
        if (defaultValue) {
            bitSet.set(0, getCount());
            this.trueElementCount = getCount();
        }
    }

    @Override
    public int getEffectiveCount() {
        return getFloorCount() * bound.getCount();
    }

    @Override
    public Boolean get(int floorIndex, int rowIndex, int columnIndex) {
        return get(getFlatIndex(floorIndex, rowIndex, columnIndex));
    }

    @Override
    public Boolean get(int flatIndex) {
        if (flatIndex < 0 || flatIndex >= getCount()) {
            return null;
        }
        return bitSet.get(flatIndex);
    }

    @Override
    public void set(int flatIndex, Boolean element) {
        rangeCheck(flatIndex);
        updateTrueElementsCount(bitSet.get(flatIndex), element);
        bitSet.get(flatIndex);
    }

    private void updateTrueElementsCount(boolean oldValue, boolean newValue) {
        if (!oldValue && newValue) {
            trueElementCount++;
        }
        if (oldValue && !newValue) {
            trueElementCount--;
        }
    }

    @Override
    public void set(int rowIndex, int columnIndex, Boolean element) {
        int flatIndex = rowIndex * bound.getColumnCount() + columnIndex;
        bitSet.set(flatIndex, element);
    }

    @Override
    public void set(int floorIndex, int rowIndex, int columnIndex, Boolean element) {
        set(getFlatIndex(floorIndex, rowIndex, columnIndex), element);
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
    public Iterator<Cursor<Index, Boolean>> iterator() {
        return new MatrixIterator();
    }

    @Override
    public Iterator<Index> indexIterator() {
        return new IndexItr();
    }

    @Override
    public Iterator<Integer> keyIterator() {
        return new KeyIterator();
    }

    @Override
    public Matrix<Boolean> getFloorMatrix(int floorIndex) {
        return null;
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

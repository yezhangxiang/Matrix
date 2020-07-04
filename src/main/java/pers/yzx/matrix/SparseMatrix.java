package pers.yzx.matrix;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SparseMatrix<E> extends AbstractMatrix<E> implements Matrix<E> {
    ArrayList<Int2ObjectOpenHashMap<E>> elementData;

    public SparseMatrix(Bound bound) {
        super(bound);
        elementData = new ArrayList<>();
        for (int i = 0; i < getFloorCount(); i++) {
            elementData.add(new Int2ObjectOpenHashMap<>());
        }
    }

    public SparseMatrix(Bound bound, int floorCount) {
        super(bound, floorCount);
        elementData = new ArrayList<>();
        for (int i = 0; i < getFloorCount(); i++) {
            elementData.add(new Int2ObjectOpenHashMap<>());
        }
    }

    @Override
    public int getEffectiveCount() {
        int effectiveCount = 0;
        for (Int2ObjectOpenHashMap<E> elementDatum : elementData) {
            effectiveCount += elementDatum.size();
        }
        return effectiveCount;
    }

    @Override
    public E get(int floorIndex, int rowIndex, int columnIndex) {
        rangeCheck(floorIndex, rowIndex, columnIndex);
        return elementData.get(floorIndex).get(getFlatIndex(DEFAULT_FLOOR_INDEX, rowIndex, columnIndex));
    }

    @Override
    public void set(int floorIndex, int rowIndex, int columnIndex, E element) {
        rangeCheck(floorIndex, rowIndex, columnIndex);
        Int2ObjectOpenHashMap<E> floorData = elementData.get(floorIndex);
        int floorFlatIndex = getFlatIndex(DEFAULT_FLOOR_INDEX, rowIndex, columnIndex);
        if (element == null) {
            floorData.remove(floorFlatIndex);
        } else {
            floorData.put(floorFlatIndex, element);
        }
    }

    @Override
    public void clear() {
        for (Int2ObjectOpenHashMap<E> elementDatum : elementData) {
            elementDatum.clear();
        }
        bound = new Bound(0, 0, 0, 0, 0);
        elementData.clear();
        floorCount = 0;
    }

    @Override
    public boolean isEmpty() {
        return elementData.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            SparseMatrix<E> sparseMatrix = (SparseMatrix<E>) super.clone();
            sparseMatrix.elementData = (ArrayList<Int2ObjectOpenHashMap<E>>) elementData.clone();
            for (int i = 0; i < sparseMatrix.elementData.size(); i++) {
                sparseMatrix.elementData.set(i, elementData.get(i));
            }
            return sparseMatrix;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public Iterator<Cursor<Index, E>> iterator() {
        return new mapIterator();
    }

    @Override
    public Iterator<Index> indexIterator() {
        return new MapIndexItr();
    }

    @Override
    public Iterator<Integer> keyIterator() {
        return new mapKeyIterator();
    }

    @Override
    public Matrix<E> getFloorMatrix(int floorIndex) {
        return null;
    }


    protected class MapIndexItr implements Iterator<Index> {
        int floorCursor = 0;
        Iterator<Map.Entry<Integer, E>> keyIterator = elementData.get(floorCursor).entrySet().iterator();

        @Override
        public boolean hasNext() {
            while (!keyIterator.hasNext()) {
                if (floorCursor < floorCount - 1) {
                    floorCursor++;
                    keyIterator = elementData.get(floorCursor).entrySet().iterator();
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Index next() {
            int floorFlatIndex = keyIterator.next().getKey();
            int rowIndex = floorFlatIndex / bound.getColumnCount();
            int columnIndex = floorFlatIndex % bound.getColumnCount();
            return new Index(floorCursor, rowIndex, columnIndex);
        }

        @Override
        public void remove() {
            keyIterator.remove();
        }
    }

    private class mapKeyIterator implements Iterator<Integer> {
        private final Iterator<Index> mapIndexItr = new MapIndexItr();

        @Override
        public boolean hasNext() {
            return mapIndexItr.hasNext();
        }

        @Override
        public Integer next() {
            return getFlatIndex(mapIndexItr.next());
        }

        @Override
        public void remove() {
            mapIndexItr.remove();
        }
    }

    private class mapIterator implements Iterator<Matrix.Cursor<Index, E>> {
        private final Iterator<Index> mapIndexItr = new MapIndexItr();

        @Override
        public boolean hasNext() {
            return mapIndexItr.hasNext();
        }

        @Override
        public Cursor<Index, E> next() {
            Index index = mapIndexItr.next();
            return new SimpleCursor<>(index, get(index));
        }

        @Override
        public void remove() {
            mapIndexItr.remove();
        }
    }
}

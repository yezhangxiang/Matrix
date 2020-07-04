package pers.yzx.matrix;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class SparseMatrix<E> extends AbstractMatrix<E> implements Matrix<E> {
    ArrayList<MyInt2ObjectOpenHashMap<E>> elementData;

    public SparseMatrix(Bound bound) {
        super(bound);
        elementData = new ArrayList<>();
        for (int i = 0; i < getFloorCount(); i++) {
            elementData.add(new MyInt2ObjectOpenHashMap<>());
        }
    }

    public SparseMatrix(Bound bound, int floorCount) {
        super(bound, floorCount);
        elementData = new ArrayList<>();
        for (int i = 0; i < getFloorCount(); i++) {
            elementData.add(new MyInt2ObjectOpenHashMap<>());
        }
    }

    @Override
    public int getEffectiveCount() {
        int effectiveCount = 0;
        for (MyInt2ObjectOpenHashMap<E> elementDatum : elementData) {
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
        MyInt2ObjectOpenHashMap<E> floorData = elementData.get(floorIndex);
        int floorFlatIndex = getFlatIndex(DEFAULT_FLOOR_INDEX, rowIndex, columnIndex);
        if (element == null) {
            floorData.remove(floorFlatIndex);
        } else {
            floorData.put(floorFlatIndex, element);
        }
    }

    @Override
    public void clear() {
        for (MyInt2ObjectOpenHashMap<E> elementDatum : elementData) {
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
            sparseMatrix.elementData = (ArrayList<MyInt2ObjectOpenHashMap<E>>) elementData.clone();
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
        IntIterator keyIterator = elementData.get(floorCursor).keySet().iterator();

        @Override
        public boolean hasNext() {
            while (!keyIterator.hasNext()) {
                if (floorCursor < floorCount - 1) {
                    floorCursor++;
                    keyIterator = elementData.get(floorCursor).keySet().iterator();
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Index next() {
            int floorFlatIndex = keyIterator.nextInt();
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

    @Override
    public Spliterator<Cursor<Index, E>> spliterator() {
        return new SparseMatrixSpliterator(this, 0, -1);
    }

    static class MyInt2ObjectOpenHashMap<V> extends Int2ObjectOpenHashMap<V> {
        int[] getKey() {
            return key;
        }

        V[] getValue() {
            return value;
        }
    }

    class SparseMatrixSpliterator implements Spliterator<Cursor<Index, E>> {
        private final SparseMatrix<E> matrix;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index

        SparseMatrixSpliterator(SparseMatrix<E> matrix, int original, int fence) {
            this.matrix = matrix;
            this.index = original;
            this.fence = fence;
        }

        private int getFence() { // initialize fence to size on first use
            int hi; // (a specialized variant appears in method forEach)
            SparseMatrix<E> lst;
            if ((hi = fence) < 0) {
                if ((lst = matrix) == null)
                    hi = fence = 0;
                else {
                    hi = fence = lst.elementData.stream().mapToInt(a -> a.getKey().length).sum();
                }
            }
            return hi;
        }

        public Spliterator<Cursor<Index, E>> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : // divide range in half unless too small
                    new SparseMatrixSpliterator(matrix, lo, index = mid);
        }

        @Override
        public boolean tryAdvance(Consumer<? super Cursor<Index, E>> action) {
            if (action == null)
                throw new NullPointerException();
            int hi = getFence();
            while (index < hi) {
                int i = index;
                index++;

                E e = null;
                int floorIndex = -1;
                int floorFlatIndex = 0;
                for (MyInt2ObjectOpenHashMap<E> elementDatum : matrix.elementData) {
                    floorIndex++;
                    int[] key = elementDatum.getKey();
                    E[] value = elementDatum.getValue();
                    if (i < key.length) {
                        if (key[i] != 0 || (i == key.length - 1 && value[i] != null)) {
                            floorFlatIndex = key[i];
                            e = value[i];
                        }
                        break;
                    } else {
                        i -= key.length;
                    }
                }
                if (e != null) {
                    int rowIndex = floorFlatIndex / bound.getColumnCount();
                    int columnIndex = floorFlatIndex % bound.getColumnCount();
                    Index index = new Index(floorIndex, rowIndex, columnIndex);
                    action.accept(new SimpleCursor<>(index, e));
                    return true;
                }
            }
            return false;
        }


        public long estimateSize() {
            return getFence() - index;
        }

        public int characteristics() {
            return Spliterator.DISTINCT;
        }
    }
}

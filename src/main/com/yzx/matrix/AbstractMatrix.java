package com.yzx.matrix;

import com.yzx.geometry.Point;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public abstract class AbstractMatrix<E> implements Matrix<E> {
    public static final int INVALID_FLAT_INDEX = -1;
    protected static final int DEFAULT_FLOOR_INDEX = 0;
    private static double floorInterval = 3;
    private static int maxFloor = 10;
    int floorCount;
    Bound bound;

    AbstractMatrix(Bound bound) {
        this.bound = bound;
        this.floorCount = 1;
    }

    public AbstractMatrix(Bound bound, int floorCount) {
        if (floorCount < 0)
            throw new IllegalArgumentException("Illegal Capacity: floor " + floorCount);
        this.bound = bound;
        this.floorCount = floorCount;
    }

    /**
     * Utility method for SimpleEntry and SimpleImmutableEntry.
     * Test for equality, checking for nulls.
     * <p>
     * NB: Do not replace with Object.equals until JDK-8015417 is resolved.
     */
    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    @Override
    public int getRowCount() {
        return bound.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return bound.getColumnCount();
    }

    @Override
    public int getCount() {
        return bound.getCount() * floorCount;
    }

    @Override
    public E get(int flatIndex) {
        rangeCheck(flatIndex);
        return get(getIndex(flatIndex));
    }

    @Override
    public E get(int rowIndex, int columnIndex) {
        return get(DEFAULT_FLOOR_INDEX, rowIndex, columnIndex);
    }

    @Override
    public E get(Index index) {
        return get(index.getFloorIndex(), index.getRowIndex(), index.getColumnIndex());
    }

    @Override
    public void set(int flatIndex, E element) {
        Index index = getIndex(flatIndex);
        set(index, element);
    }

    @Override
    public void set(int rowIndex, int columnIndex, E element) {
        set(DEFAULT_FLOOR_INDEX, rowIndex, columnIndex, element);
    }

    @Override
    public void set(Index index, E element) {
        set(index.getFloorIndex(), index.getRowIndex(), index.getColumnIndex(), element);
    }

    protected void rangeCheck(int floorIndex, int rowIndex, int columnIndex) {
        if (floorIndex > maxFloor || rowIndex >= bound.getRowCount() || columnIndex >= bound.getColumnCount() ||
                floorIndex < 0 || rowIndex < 0 || columnIndex < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(rowIndex, columnIndex));
    }

    void rangeCheck(int flatIndex) {
        if (flatIndex < 0 || flatIndex > getCount()) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(flatIndex));
        }
    }

    private String outOfBoundsMsg(int flatIndex) {
        return "Flat index: " + flatIndex + ", matrix size: " + getCount();
    }

    private String outOfBoundsMsg(int rowIndex, int columnIndex) {
        return "Row index: " + rowIndex + ", row size: " + getRowCount() +
                "; Column index: " + columnIndex + ", column size: " + getColumnCount();
    }

    @Override
    public double getTopLeftX() {
        return bound.getTopLeftX();
    }

    @Override
    public double getTopLeftY() {
        return bound.getTopLeftY();
    }

    @Override
    public double getBottomRightX() {
        return bound.getBottomRightX();
    }

    @Override
    public double getBottomRightY() {
        return bound.getBottomRightY();
    }

    @Override
    public int getFloorCount() {
        return floorCount;
    }

    @Override
    public int getResolution() {
        return bound.getResolution();
    }

    @Override
    public boolean isInside(Point point) {
        return point.getZ() >= 0 && point.getZ() < getFloorCount() * floorInterval && bound.isInside(point);
    }

    @Override
    public int getStartRow(Matrix matrix) {
        return (int) (bound.getTopLeftY() - matrix.getTopLeftY()) / bound.getResolution();
    }

    @Override
    public int getStartColumn(Matrix matrix) {
        return (int) (-bound.getTopLeftX() + matrix.getTopLeftX()) / bound.getResolution();
    }

    @Override
    public int getFlatIndex(Index index) {
        return index.getFloorIndex() * bound.getCount() + index.getRowIndex() * bound.getColumnCount() + index.getColumnIndex();
    }

    @Override
    public int getFlatIndex(int rowIndex, int columnIndex) {
        return rowIndex * bound.getColumnCount() + columnIndex;
    }

    @Override
    public int getFlatIndex(int floorIndex, int rowIndex, int columnIndex) {
        return floorIndex * bound.getCount() + rowIndex * bound.getColumnCount() + columnIndex;
    }

    @Override
    public Index getIndex(int flatIndex) {
        if (flatIndex < 0 || flatIndex >= getCount()) {
            return null;
        }
        int floorIndex = flatIndex / bound.getCount();
        int floorFlatIndex = flatIndex % bound.getCount();
        int rowIndex = floorFlatIndex / bound.getColumnCount();
        int columnIndex = floorFlatIndex % bound.getColumnCount();
        return new Index(floorIndex, rowIndex, columnIndex);
    }

    @Override
    public Index getIndex(Matrix<?> matrix, Index matrixIndex) {
        double x = matrix.getTopLeftX() + matrixIndex.getColumnIndex() * matrix.getResolution();
        double y = matrix.getTopLeftY() - matrixIndex.getRowIndex() * matrix.getResolution();
        int rowIndex = (int) ((x - bound.getTopLeftX()) / bound.getResolution());
        int columnIndex = (int) ((bound.getTopLeftY() - y) / bound.getResolution());
        if (rowIndex < 0 || rowIndex >= bound.getRowCount() || columnIndex < 0 || columnIndex >= bound.getColumnCount()) {
            return null;
        }
        return new Index(matrixIndex.getFloorIndex(), rowIndex, columnIndex);
    }

    @Override
    public Index getIndex(Point point) {
        if (!isInside(point)) {
            return null;
        }
        int floorIndex = (int) (point.getZ() / floorInterval);
        int rowIndex = (int) ((point.getX() - bound.getTopLeftX()) / bound.getResolution());
        int columnIndex = (int) ((bound.getTopLeftY() - point.getY()) / bound.getResolution());
        return new Index(floorIndex, rowIndex, columnIndex);
    }

    @Override
    public Point getPoint(Index index) {
        double x = bound.getTopLeftX() + index.getColumnIndex() * bound.getResolution();
        double y = bound.getTopLeftY() - index.getRowIndex() * bound.getResolution();
        double z = index.getFloorIndex() * floorInterval;
        return new Point(x, y, z);
    }

    @Override
    public Bound getBound() {
        return bound;
    }

    /**
     * An Cursor maintaining a index and a element.  The element may be
     * changed using the <tt>setElement</tt> method.  This class
     * facilitates the process of building custom matrix
     * implementations. For example, it may be convenient to return
     * arrays of <tt>SimpleCursor</tt> instances in method
     * <tt>Matrix.entrySet().toArray</tt>.
     *
     * @since 1.6
     */
    public static class SimpleCursor<Index, E>
            implements Cursor<Index, E>, java.io.Serializable {
        private static final long serialVersionUID = -8499721149061103585L;

        private final Index index;
        private E element;

        /**
         * Creates an cursor representing a mapping from the specified
         * index to the specified element.
         *
         * @param index   the index represented by this cursor
         * @param element the element represented by this cursor
         */
        public SimpleCursor(Index index, E element) {
            this.index = index;
            this.element = element;
        }

        /**
         * Creates an cursor representing the same mapping as the
         * specified cursor.
         *
         * @param cursor the cursor to copy
         */
        public SimpleCursor(Cursor<? extends Index, ? extends E> cursor) {
            this.index = cursor.getIndex();
            this.element = cursor.getElement();
        }

        /**
         * Returns the index corresponding to this cursor.
         *
         * @return the index corresponding to this cursor
         */
        public Index getIndex() {
            return index;
        }

        /**
         * Returns the element corresponding to this cursor.
         *
         * @return the element corresponding to this cursor
         */
        public E getElement() {
            return element;
        }

        /**
         * Replaces the element corresponding to this cursor with the specified
         * element.
         *
         * @param element new element to be stored in this cursor
         * @return the old element corresponding to the cursor
         */
        public E setElement(E element) {
            E oldValue = this.element;
            this.element = element;
            return oldValue;
        }

        /**
         * Compares the specified object with this cursor for equality.
         * Returns {@code true} if the given object is also a matrix cursor and
         * the two entries represent the same mapping.  More formally, two
         * entries {@code e1} and {@code e2} represent the same mapping
         * if<pre>
         *   (e1.getIndex()==null ?
         *    e2.getIndex()==null :
         *    e1.getIndex().equals(e2.getIndex()))
         *   &amp;&amp;
         *   (e1.getElement()==null ?
         *    e2.getElement()==null :
         *    e1.getElement().equals(e2.getElement()))</pre>
         * This ensures that the {@code equals} method works properly across
         * different implementations of the {@code Matrix.Cursor} interface.
         *
         * @param o object to be compared for equality with this matrix cursor
         * @return {@code true} if the specified object is equal to this matrix
         * cursor
         * @see #hashCode
         */
        public boolean equals(Object o) {
            if (!(o instanceof Matrix.Cursor))
                return false;
            Matrix.Cursor<?, ?> e = (Matrix.Cursor<?, ?>) o;
            return eq(index, e.getIndex()) && eq(element, e.getElement());
        }

        /**
         * Returns the hash code element for this matrix cursor.  The hash code
         * of a matrix cursor {@code e} is defined to be: <pre>
         *   (e.getIndex()==null   ? 0 : e.getIndex().hashCode()) ^
         *   (e.getElement()==null ? 0 : e.getElement().hashCode())</pre>
         * This ensures that {@code e1.equals(e2)} implies that
         * {@code e1.hashCode()==e2.hashCode()} for any two Entries
         * {@code e1} and {@code e2}, as required by the general
         * contract of {@link Object#hashCode}.
         *
         * @return the hash code element for this matrix cursor
         * @see #equals
         */
        public int hashCode() {
            return (index == null ? 0 : index.hashCode()) ^
                    (element == null ? 0 : element.hashCode());
        }

        /**
         * Returns a String representation of this matrix cursor.  This
         * implementation returns the string representation of this
         * cursor's index followed by the equals character ("<tt>=</tt>")
         * followed by the string representation of this cursor's element.
         *
         * @return a String representation of this matrix cursor
         */
        public String toString() {
            return index + "=" + element;
        }
    }

    class KeyIterator implements Iterator<Integer> {
        private Iterator<Index> indexItr = new IndexItr();

        @Override
        public boolean hasNext() {
            return indexItr.hasNext();
        }

        @Override
        public Integer next() {
            return getFlatIndex(indexItr.next());
        }

        @Override
        public void remove() {
            indexItr.remove();
        }
    }

    @Override
    public Spliterator<Cursor<Index, E>> spliterator() {
        return new MatrixSpliterator(this, 0, -1);
    }

    class MatrixSpliterator implements Spliterator<Cursor<Index, E>> {
        private final Matrix<E> matrix;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index

        MatrixSpliterator(Matrix<E> matrix, int original, int fence) {
            this.matrix = matrix;
            this.index = original;
            this.fence = fence;
        }

        private int getFence() { // initialize fence to size on first use
            int hi; // (a specialized variant appears in method forEach)
            Matrix<E> lst;
            if ((hi = fence) < 0) {
                if ((lst = matrix) == null)
                    hi = fence = 0;
                else {
                    hi = fence = lst.getCount();
                }
            }
            return hi;
        }

        public Spliterator<Cursor<Index, E>> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : // divide range in half unless too small
                    new MatrixSpliterator(matrix, lo, index = mid);
        }

        @Override
        public boolean tryAdvance(Consumer<? super Cursor<Index, E>> action) {
            if (action == null)
                throw new NullPointerException();
            int hi = getFence(), i = index;
            while (i < hi) {
                index = i + 1;
                E e = matrix.get(i);
                if (e != null) {
                    Index index = getIndex(i);
                    action.accept(new SimpleCursor<>(index, e));
                    return true;
                }
                i += 1;
            }
            return false;
        }


        public long estimateSize() {
            return (long) (getFence() - index);
        }

        public int characteristics() {
            return Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

    class IndexItr implements Iterator<Index> {
        int floorCursor = 0;
        int lastFloorRet = -1;
        int lastRowRet = -1;
        int lastColumnRet = -1;
        private int rowCursor = 0;
        private int columnCursor = 0;

        @Override
        public boolean hasNext() {
            for (; floorCursor < floorCount; floorCursor++) {
                for (; rowCursor < bound.getRowCount(); rowCursor++) {
                    for (; columnCursor < bound.getColumnCount(); columnCursor++) {
                        if (null != get(floorCursor, rowCursor, columnCursor)) {
                            return true;
                        }
                    }
                    columnCursor = 0;
                }
                rowCursor = 0;
            }
            return false;
        }

        @Override
        public Index next() {
            Object o;
            int floorIndex;
            int rowIndex;
            int columnIndex;
            do {
                floorIndex = floorCursor;
                rowIndex = rowCursor;
                columnIndex = columnCursor;

                o = get(floorIndex, rowIndex, columnIndex);
                if (columnCursor == bound.getColumnCount() - 1) {
                    if (rowCursor == bound.getRowCount() - 1) {
                        floorCursor++;
                        rowCursor = 0;
                    } else {
                        rowCursor++;
                    }
                    columnCursor = 0;
                } else {
                    columnCursor++;
                }
            } while (null == o);

            lastFloorRet = floorIndex;
            lastRowRet = rowIndex;
            lastColumnRet = columnIndex;
            return new Index(floorIndex, rowIndex, columnIndex);
        }

        @Override
        public void remove() {
            if (lastFloorRet < 0 || lastRowRet < 0 || lastColumnRet < 0) {
                throw new IllegalStateException();
            }
            set(lastFloorRet, lastRowRet, lastColumnRet, null);
            lastFloorRet = -1;
            lastRowRet = -1;
            lastColumnRet = -1;
        }
    }

    class MatrixIterator implements Iterator<Matrix.Cursor<Index, E>> {
        private Iterator<Index> indexItr = new IndexItr();

        @Override
        public boolean hasNext() {
            return indexItr.hasNext();
        }

        @Override
        public Cursor<Index, E> next() {
            Index index = indexItr.next();
            return new SimpleCursor<>(index, get(index));
        }

        @Override
        public void remove() {
            indexItr.remove();
        }
    }
}


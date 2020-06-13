package com.yzx.matrix;

import com.yzx.geometry.Point;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

public interface Matrix<E> extends Iterable<Matrix.Cursor<Index, E>> {

    // Query Operations
    double getTopLeftX();

    double getTopLeftY();

    double getBottomRightX();

    double getBottomRightY();

    int getFloorCount();

    int getRowCount();

    int getColumnCount();

    int getCount();

    int getEffectiveCount();

    int getResolution();

    boolean isEmpty();

    boolean isInside(Point point);

    // Position Access Operations
    E get(Index index);

    E get(int rowIndex, int columnIndex);

    E get(int floorIndex, int rowIndex, int columnIndex);

    E get(int flatIndex);

    void set(Index index, E element);

    void set(int rowIndex, int columnIndex, E element);

    void set(int floorIndex, int rowIndex, int columnIndex, E element);

    void set(int flatIndex, E element);

    void clear();

    // Matrix Iterators
    Iterator<Matrix.Cursor<Index, E>> iterator();

    Iterator<Index> indexIterator();

    Iterator<Integer> keyIterator();

    // Search Operations
    int getStartRow(Matrix matrix);

    int getStartColumn(Matrix matrix);

    int getFlatIndex(Index index);

    int getFlatIndex(int rowIndex, int columnIndex);

    int getFlatIndex(int floorIndex, int rowIndex, int columnIndex);

    Index getIndex(int flatIndex);

    Index getIndex(Matrix<?> matrix, Index matrixIndex);

    Index getIndex(Point point);

    Point getPoint(Index index);

    // View
    Bound getBound();

    Matrix<E> getFloorMatrix(int floorIndex);

    /**
     * A map entry (index-value pair).  The <tt>Matrix.entrySet</tt> method returns
     * a collection-view of the map, whose elements are of this class.  The
     * <i>only</i> way to obtain a reference to a map entry is from the
     * iterator of this collection-view.  These <tt>Matrix.Cursor</tt> objects are
     * valid <i>only</i> for the duration of the iteration; more formally,
     * the behavior of a map entry is undefined if the backing map has been
     * modified after the entry was returned by the iterator, except through
     * the <tt>setElement</tt> operation on the map entry.
     *
     * @since 1.2
     */
    interface Cursor<Index, E> {
        /**
         * Returns a comparator that compares {@link Matrix.Cursor} in natural order on index.
         * <p>
         * <p>The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with a null index.
         *
         * @param <Index> the {@link Comparable} type of then map keys
         * @param <E>     the type of the map values
         * @return a comparator that compares {@link Matrix.Cursor} in natural order on index.
         * @see Comparable
         * @since 1.8
         */
        public static <Index extends Comparable<? super Index>, E> Comparator<Cursor<Index, E>> comparingByIndex() {
            return (Comparator<Matrix.Cursor<Index, E>> & Serializable)
                    (c1, c2) -> c1.getIndex().compareTo(c2.getIndex());
        }

        /**
         * Returns a comparator that compares {@link Matrix.Cursor} in natural order on value.
         * <p>
         * <p>The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with null values.
         *
         * @param <Index> the type of the map keys
         * @param <E>     the {@link Comparable} type of the map values
         * @return a comparator that compares {@link Matrix.Cursor} in natural order on value.
         * @see Comparable
         * @since 1.8
         */
        public static <Index, E extends Comparable<? super E>> Comparator<Matrix.Cursor<Index, E>> comparingByElement() {
            return (Comparator<Matrix.Cursor<Index, E>> & Serializable)
                    (c1, c2) -> c1.getElement().compareTo(c2.getElement());
        }

        /**
         * Returns a comparator that compares {@link Matrix.Cursor} by index using the given
         * {@link Comparator}.
         * <p>
         * <p>The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param <Index> the type of the map keys
         * @param <E>     the type of the map values
         * @param cmp     the index {@link Comparator}
         * @return a comparator that compares {@link Matrix.Cursor} by the index.
         * @since 1.8
         */
        public static <Index, E> Comparator<Matrix.Cursor<Index, E>> comparingByIndex(Comparator<? super Index> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Matrix.Cursor<Index, E>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getIndex(), c2.getIndex());
        }

        /**
         * Returns a comparator that compares {@link Matrix.Cursor} by value using the given
         * {@link Comparator}.
         * <p>
         * <p>The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param <Index> the type of the map keys
         * @param <E>     the type of the map values
         * @param cmp     the value {@link Comparator}
         * @return a comparator that compares {@link Matrix.Cursor} by the value.
         * @since 1.8
         */
        public static <Index, E> Comparator<Matrix.Cursor<Index, E>> comparingByElement(Comparator<? super E> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Matrix.Cursor<Index, E>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getElement(), c2.getElement());
        }

        /**
         * Returns the index corresponding to this entry.
         *
         * @return the index corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        Index getIndex();

        /**
         * Returns the value corresponding to this entry.  If the mapping
         * has been removed from the backing map (by the iterator's
         * <tt>remove</tt> operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        E getElement();

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation).  (Writes through to the map.)  The
         * behavior of this call is undefined if the mapping has already been
         * removed from the map (by the iterator's <tt>remove</tt> operation).
         *
         * @param value new value to be stored in this entry
         * @return old value corresponding to the entry
         * @throws UnsupportedOperationException if the <tt>put</tt> operation
         *                                       is not supported by the backing map
         * @throws ClassCastException            if the class of the specified value
         *                                       prevents it from being stored in the backing map
         * @throws NullPointerException          if the backing map does not permit
         *                                       null values, and the specified value is null
         * @throws IllegalArgumentException      if some property of this value
         *                                       prevents it from being stored in the backing map
         * @throws IllegalStateException         implementations may, but are not
         *                                       required to, throw this exception if the entry has been
         *                                       removed from the backing map.
         */
        E setElement(E value);

        /**
         * Compares the specified object with this entry for equality.
         * Returns <tt>true</tt> if the given object is also a map entry and
         * the two entries represent the same mapping.  More formally, two
         * entries <tt>e1</tt> and <tt>e2</tt> represent the same mapping
         * if<pre>
         *     (e1.getIndex()==null ?
         *      e2.getIndex()==null : e1.getIndex().equals(e2.getIndex()))  &amp;&amp;
         *     (e1.getElement()==null ?
         *      e2.getElement()==null : e1.getElement().equals(e2.getElement()))
         * </pre>
         * This ensures that the <tt>equals</tt> method works properly across
         * different implementations of the <tt>Matrix.Cursor</tt> interface.
         *
         * @param o object to be compared for equality with this map entry
         * @return <tt>true</tt> if the specified object is equal to this map
         * entry
         */
        boolean equals(Object o);

        /**
         * Returns the hash code value for this map entry.  The hash code
         * of a map entry <tt>e</tt> is defined to be: <pre>
         *     (e.getIndex()==null   ? 0 : e.getIndex().hashCode()) ^
         *     (e.getElement()==null ? 0 : e.getElement().hashCode())
         * </pre>
         * This ensures that <tt>e1.equals(e2)</tt> implies that
         * <tt>e1.hashCode()==e2.hashCode()</tt> for any two Entries
         * <tt>e1</tt> and <tt>e2</tt>, as required by the general
         * contract of <tt>Object.hashCode</tt>.
         *
         * @return the hash code value for this map entry
         * @see Object#hashCode()
         * @see Object#equals(Object)
         * @see #equals(Object)
         */
        int hashCode();
    }
}

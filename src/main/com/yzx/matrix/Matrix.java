package com.yzx.matrix;

import java.util.Iterator;

public interface Matrix<E> {

    double getTopLeftX();
    double getTopLeftY();
    double getBottomRightX();
    double getBottomRightY();
    int getRowCount();
    int getColumnCount();
    int getCount();
    int getEffectiveCount();
    int getResolution();
    boolean isEmpty();
    boolean isInside(Point point);


    E get(Index index);
    E get(int rowIndex, int columnIndex);
    E get(int flatIndex);

    void set(Index index, E element);
    void set(int rowIndex, int columnIndex, E element);
    void set(int flatIndex, E element);

    void clear();

    Iterator<E> iterator();
    Iterator<Integer> keyIterator();

    int getStartRow(Matrix matrix);
    int getStartColumn(Matrix matrix);

    int getFlatIndex(Index index);
    int getFlatIndex(int rowIndex, int columnIndex);

    Index getIndex(int flatIndex);
    Index getIndex(Matrix<?> matrix, Index matrixIndex);
    Index getIndex(Point point);

    Point getPoint(Index index);

    Bound getBound();

    void crop();
}

package com.yzx.matrix;

import java.util.Iterator;

public interface Matrix<E> {

    double getTopLeftX();
    double getTopLeftY();
    double getBottomRightX();
    double getBottomRightY();
    int getRowCount();
    int getColumnCount();
    int getEffectiveCount();
    int getResolution();
    E get(int rowIndex, int columnIndex);

    E get(int flatIndex);
    E get(double x, double y);

    void set(int rowIndex, int columnIndex, E element);
    void set(int flatIndex, E element);
    void set(double x, double y, E element);
    boolean isEmpty();

    Iterator<E> iterator();

    Iterator<Integer> keyIterator();
    int getStartRow(Matrix matrix);
    int getStartColumn(Matrix matrix);

    void crop();
}

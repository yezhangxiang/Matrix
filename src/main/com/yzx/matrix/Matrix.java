package com.yzx.matrix;

import com.yzx.geometry.Point;

import java.util.Iterator;

public interface Matrix<E> {

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
    Iterator<E> iterator();
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

}

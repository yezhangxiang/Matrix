package com.yzx.matrix;

public class Index {
    private final int rowIndex;
    private final int columnIndex;

    public Index(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}

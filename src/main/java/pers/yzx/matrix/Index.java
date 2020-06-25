package pers.yzx.matrix;

public class Index {
    private final int floorIndex;
    private final int rowIndex;
    private final int columnIndex;

    public Index(int rowIndex, int columnIndex) {
        this.floorIndex = 0;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public Index(int floorIndex, int rowIndex, int columnIndex) {
        this.floorIndex = floorIndex;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getFloorIndex() {
        return floorIndex;
    }
}

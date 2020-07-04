package pers.yzx.matrix;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class SparseMatrixTest {

    @Test
    public void spliterator() {
        // init
        Bound bound = new Bound(0.0, 0.0, 10, 2, 1);
        System.out.println("test stream for sparse matrix:");
        Matrix<String> matrix = new SparseMatrix<>(bound);
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                matrix.set(i, j, "r" + i + "_c" + j);
            }
        }

        // test stream sorted
        long startTime = System.currentTimeMillis();
        List<String> sortedList = matrix.stream().map(Matrix.Cursor::getElement).sorted(Comparator.naturalOrder()).collect(toList());
        long endTime = System.currentTimeMillis();
        System.out.println("stream sorted takes " + (endTime - startTime));
        assertEquals(matrix.getCount(), sortedList.size());
    }
}
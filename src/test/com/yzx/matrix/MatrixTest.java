package com.yzx.matrix;

import com.yzx.geometry.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class MatrixTest {
    private SparseMatrix<String> sparseMatrix;
    private ArrayMatrix<String> arrayMatrix;
    private BitMatrix bitMatrix;

    @Before
    public void setUp() throws Exception {
        Bound bound = new Bound(0.0, 0.0, 5, 6, 1);
        sparseMatrix = new SparseMatrix<>(bound);
        sparseMatrix.set(0, 1, "r0_c1");
        sparseMatrix.set(4, 1, "r4_c1");
        sparseMatrix.set(3, 5, "r3_c5");

        arrayMatrix = new ArrayMatrix<>(bound);
        arrayMatrix.set(0, 1, "r0_c1");
        arrayMatrix.set(4, 1, "r4_c1");
        arrayMatrix.set(3, 5, "r3_c5");

        Bound bitBound = new Bound(-3.0, 4.0, 7, 6, 1);
        bitMatrix = new BitMatrix(bitBound);
        bitMatrix.set(0, 1, true);
        bitMatrix.set(4, 1, true);
        bitMatrix.set(3, 5, true);
    }

    @Test
    public void getCount() throws Exception {
        assertEquals(30, sparseMatrix.getCount());
        assertEquals(30, arrayMatrix.getCount());
        assertEquals(42, bitMatrix.getCount());
    }

    @Test
    public void clear() throws Exception {
        sparseMatrix.clear();
        assertEquals(0, sparseMatrix.getEffectiveCount());
        assertEquals(0, sparseMatrix.getCount());

        arrayMatrix.clear();
        assertEquals(0, arrayMatrix.getEffectiveCount());
        assertEquals(0, arrayMatrix.getCount());

        bitMatrix.clear();
        assertEquals(0, bitMatrix.getEffectiveCount());
        assertEquals(0, bitMatrix.getCount());
    }

    @Test
    public void getFlatIndex() throws Exception {
        assertEquals(0, sparseMatrix.getFlatIndex(new Index(0, 0)));
        assertEquals(0, sparseMatrix.getFlatIndex(0, 0));
    }

    @Test
    public void getIndex() throws Exception {
        Index index = sparseMatrix.getIndex(arrayMatrix, new Index(3, 4));
        assert index.getRowIndex() == 3;
        assert index.getColumnIndex() == 4;

        Index index1 = sparseMatrix.getIndex(0);
        assert index1.getRowIndex() == 0;
        assert index1.getColumnIndex() == 0;

        Index index2 = sparseMatrix.getIndex(new Point(3.4, 2.4));
        assert index2.getRowIndex() == 3;
        assert index2.getColumnIndex() == 4;
    }

    @Test
    public void getPoint() throws Exception {
        Point point = sparseMatrix.getPoint(new Index(0, 0));
        assertEquals(0.0, point.getX(), 0.0);
        assertEquals(0.0, point.getX(), 0.0);

    }

    @Test
    public void getBound() throws Exception {
        Bound bound = sparseMatrix.getBound();
        assert bound.getTopLeftX() == 0.0;
        assert bound.getTopLeftY() == 0.0;
        assert bound.getResolution() == 1;
        assert bound.getRowCount() == 5;
        assert bound.getColumnCount() == 6;
    }

    @Test
    public void getTopLeftX() throws Exception {
        assertEquals(0.0, sparseMatrix.getTopLeftX(), 0.0);
        assertEquals(0.0, arrayMatrix.getTopLeftX(), 0.0);
        assertEquals(-3.0, bitMatrix.getTopLeftX(), 0.0);
    }

    @Test
    public void getTopLeftY() throws Exception {
        assertEquals(0.0, sparseMatrix.getTopLeftY(), 0.0);
        assertEquals(0.0, arrayMatrix.getTopLeftY(), 0.0);
        assertEquals(4.0, bitMatrix.getTopLeftY(), 0.0);
    }

    @Test
    public void getBottomRightX() throws Exception {
        assertEquals(6.0, sparseMatrix.getBottomRightX(), 0.0);
        assertEquals(6.0, sparseMatrix.getBottomRightX(), 0.0);
        assertEquals(3.0, bitMatrix.getBottomRightX(), 0.0);
    }

    @Test
    public void getBottomRightY() throws Exception {
        assertEquals(-5.0, sparseMatrix.getBottomRightY(), 0.0);
        assertEquals(-5.0, sparseMatrix.getBottomRightY(), 0.0);
        assertEquals(-3.0, bitMatrix.getBottomRightY(), 0.0);
    }

    @Test
    public void getRowCount() throws Exception {
        assertEquals(5, sparseMatrix.getRowCount());
        assertEquals(5, arrayMatrix.getRowCount());
        assertEquals(7, bitMatrix.getRowCount());
    }

    @Test
    public void getColumnCount() throws Exception {
        assertEquals(6, sparseMatrix.getColumnCount());
        assertEquals(6, arrayMatrix.getColumnCount());
        assertEquals(6, bitMatrix.getColumnCount());
    }

    @Test
    public void getEffectiveCount() throws Exception {
        assertEquals(3, sparseMatrix.getEffectiveCount());
        assertEquals(3, arrayMatrix.getEffectiveCount());
        assertEquals(42, bitMatrix.getEffectiveCount());
    }

    @Test
    public void getResolution() throws Exception {
        assertEquals(1, sparseMatrix.getResolution());
        assertEquals(1, arrayMatrix.getResolution());
        assertEquals(1, bitMatrix.getResolution());
    }

    @Test
    public void get() throws Exception {
        assertEquals("r0_c1", sparseMatrix.get(0, 1));
        assertEquals("r0_c1", arrayMatrix.get(0, 1));
        assertEquals("r0_c1", sparseMatrix.get(1));
        assertEquals("r0_c1", arrayMatrix.get(1));
        assertTrue(bitMatrix.get(0, 1));
    }

    @Test
    public void set() throws Exception {
        sparseMatrix.set(3, 3, "r3_c3");
        assertEquals("r3_c3", sparseMatrix.get(3, 3));

        arrayMatrix.set(3, 3, "r3_c3");
        assertEquals("r3_c3", arrayMatrix.get(3, 3));

        bitMatrix.set(3, 3, true);
        assertEquals(true, bitMatrix.get(3, 3));
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(!sparseMatrix.isEmpty());
        assertTrue(!arrayMatrix.isEmpty());
        assertTrue(!bitMatrix.isEmpty());
    }

    @Test
    public void forEach() {
        int num = 0;
        for (Matrix.Cursor<Index, String> cursor : sparseMatrix) {
            String s = cursor.getElement();
            assertNotNull(s);
            num++;
        }
        assertEquals(sparseMatrix.getEffectiveCount(), num);
        num = 0;
        for (Matrix.Cursor<Index, String> cursor : arrayMatrix) {
            String s = cursor.getElement();
            assertNotNull(s);
            num++;
        }
        assertEquals(arrayMatrix.getEffectiveCount(), num);

        num = 0;
        for (Matrix.Cursor<Index, Boolean> cursor : bitMatrix) {
            Boolean bool = cursor.getElement();
            assertNotNull(bool);
            num++;
        }
        assertEquals(bitMatrix.getEffectiveCount(), num);
    }

    @Test
    public void iterator() throws Exception {
        int num = 0;
        Iterator<Matrix.Cursor<Index, String>> iterator = sparseMatrix.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next().getElement();
            assertNotNull(s);
            num++;
        }
        assertEquals(sparseMatrix.getEffectiveCount(), num);

        num = 0;
        iterator = arrayMatrix.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next().getElement();
            assertNotNull(s);
            num++;
        }
        assertEquals(arrayMatrix.getEffectiveCount(), num);

        num = 0;
        Iterator<Matrix.Cursor<Index, Boolean>> bitMatrixIterator = bitMatrix.iterator();
        while (bitMatrixIterator.hasNext()) {
            Boolean s = bitMatrixIterator.next().getElement();
            assertNotNull(s);
            num++;
        }
        assertEquals(bitMatrix.getEffectiveCount(), num);
    }

    @Test
    public void keyIterator() throws Exception {
        int num = 0;
        Iterator<Integer> keyIterator = sparseMatrix.keyIterator();
        while (keyIterator.hasNext()) {
            Integer flatIndex = keyIterator.next();
            String s = sparseMatrix.get(flatIndex);
            assertNotNull(s);
            num++;
        }
        assertEquals(sparseMatrix.getEffectiveCount(), num);

        num = 0;
        keyIterator = arrayMatrix.keyIterator();
        while (keyIterator.hasNext()) {
            Integer flatIndex = keyIterator.next();
            String s = arrayMatrix.get(flatIndex);
            assertNotNull(s);
            num++;
        }
        assertEquals(arrayMatrix.getEffectiveCount(), num);

        num = 0;
        keyIterator = bitMatrix.keyIterator();
        while (keyIterator.hasNext()) {
            Integer flatIndex = keyIterator.next();
            Boolean s = bitMatrix.get(flatIndex);
            assertNotNull(s);
            num++;
        }
        assertEquals(bitMatrix.getEffectiveCount(), num);
    }

    @Test
    public void getStartRow() throws Exception {
        assertEquals(0, sparseMatrix.getStartRow(arrayMatrix));
        assertEquals(0, arrayMatrix.getStartRow(sparseMatrix));
        assertEquals(4, bitMatrix.getStartRow(sparseMatrix));
    }

    @Test
    public void getStartColumn() throws Exception {
        assertEquals(0, sparseMatrix.getStartColumn(arrayMatrix));
        assertEquals(0, arrayMatrix.getStartColumn(sparseMatrix));
        assertEquals(3, bitMatrix.getStartColumn(sparseMatrix));
    }

    @Test
    public void streamSparseMatrix() {
        // init
        Bound bound = new Bound(0.0, 0.0, 1000, 1000, 1);
        System.out.println("test stream for sparse matrix:");
        Matrix<String> matrix = new SparseMatrix<>(bound);
        stream(matrix);
    }

    @Test
    public void streamArrayMatrix() {
        // init
        Bound bound = new Bound(0.0, 0.0, 1000, 1000, 1);
        System.out.println("test stream for array matrix:");
        Matrix<String> matrix = new ArrayMatrix<>(bound);
        stream(matrix);
    }

    private void stream(Matrix<String> arrayMatrix) {
        for (int i = 0; i < arrayMatrix.getRowCount(); i++) {
            for (int j = 0; j < arrayMatrix.getColumnCount(); j++) {
                arrayMatrix.set(i, j,"r"+i+"_c"+j);
            }
        }

        // test stream sorted
        long startTime = System.currentTimeMillis();
        List<String> sortedList = arrayMatrix.stream().map(Matrix.Cursor::getElement).sorted(Comparator.naturalOrder()).collect(toList());
        long endTime = System.currentTimeMillis();
        System.out.println("stream sorted takes " +  (endTime - startTime));
        assertEquals(arrayMatrix.getCount(), sortedList.size());

        // test parallel stream sorted
        startTime = System.currentTimeMillis();
        List<String> parallelSortedList = arrayMatrix.parallelStream().map(Matrix.Cursor::getElement).sorted(Comparator.naturalOrder()).collect(toList());
        endTime = System.currentTimeMillis();
        System.out.println("parallel sorted stream takes " +  (endTime - startTime));
        assertEquals(arrayMatrix.getCount(), parallelSortedList.size());

        assertEquals(sortedList, parallelSortedList);

        Predicate<String > filterPredicate = t -> t.contains("1") || t.contains("3") || t.contains("5") ||
                t.contains("7") || t.contains("9");
        // test stream filter
        startTime = System.currentTimeMillis();
        List<String> filterList = arrayMatrix.stream().
                map(Matrix.Cursor::getElement).filter(filterPredicate).collect(toList());
        endTime = System.currentTimeMillis();
        System.out.println("stream filter takes " +  (endTime - startTime));

        // test parallel stream filter
        startTime = System.currentTimeMillis();
        List<String> parallelFilterList = arrayMatrix.parallelStream().
                map(Matrix.Cursor::getElement).filter(filterPredicate).collect(toList());
        endTime = System.currentTimeMillis();
        System.out.println("parallel stream filter takes " +  (endTime - startTime));

        // test stream foreach filter
        startTime = System.currentTimeMillis();
        List<String> foreachFilterList = new ArrayList<>();
        for (Matrix.Cursor<Index, String> cursor : arrayMatrix) {
            if (filterPredicate.test(cursor.getElement())) {
                foreachFilterList.add(cursor.getElement());
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println("foreach filter takes " +  (endTime - startTime));

        assertEquals(filterList, parallelFilterList);
        assertEquals(filterList, foreachFilterList);
    }
}
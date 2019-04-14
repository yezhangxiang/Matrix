package com.yzx.matrix;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by kele on 2019/4/13.
 */
public class MatrixTest {
    MapMatrix<String> mapMatrix;
    ArrayMatrix<String> arrayMatrix;
    BitMatrix bitMatrix;

    @Before
    public void setUp() throws Exception {
        mapMatrix = new MapMatrix<>(5, 6, 1, 0.0, 0.0);
        mapMatrix.set(0, 1, "r0_c1");
        mapMatrix.set(4, 1, "r4_c1");
        mapMatrix.set(3, 5, "r3_c5");

        arrayMatrix = new ArrayMatrix<>(5, 6, 1, 0.0, 0.0);
        arrayMatrix.set(0, 1, "r0_c1");
        arrayMatrix.set(4, 1, "r4_c1");
        arrayMatrix.set(3, 5, "r3_c5");

        bitMatrix = new BitMatrix(7, 6, 1, -3.0, 4.0);
        bitMatrix.set(0, 1, true);
        bitMatrix.set(4, 1, true);
        bitMatrix.set(3, 5, true);
    }

    @Test
    public void getTopLeftX() throws Exception {
        assertEquals(0.0, mapMatrix.getTopLeftX(), 0.0);
        assertEquals(0.0, arrayMatrix.getTopLeftX(), 0.0);
        assertEquals(-3.0, bitMatrix.getTopLeftX(), 0.0);
    }

    @Test
    public void getTopLeftY() throws Exception {
        assertEquals(0.0, mapMatrix.getTopLeftY(), 0.0);
        assertEquals(0.0, arrayMatrix.getTopLeftY(), 0.0);
        assertEquals(4.0, bitMatrix.getTopLeftY(), 0.0);
    }

    @Test
    public void getBottomRightX() throws Exception {
        assertEquals(6.0, mapMatrix.getBottomRightX(), 0.0);
        assertEquals(6.0, mapMatrix.getBottomRightX(), 0.0);
        assertEquals(3.0, bitMatrix.getBottomRightX(), 0.0);
    }

    @Test
    public void getBottomRightY() throws Exception {
        assertEquals(-5.0, mapMatrix.getBottomRightY(), 0.0);
        assertEquals(-5.0, mapMatrix.getBottomRightY(), 0.0);
        assertEquals(-3.0, bitMatrix.getBottomRightY(), 0.0);
    }

    @Test
    public void getRowCount() throws Exception {
        assertEquals(5, mapMatrix.getRowCount());
        assertEquals(5, arrayMatrix.getRowCount());
        assertEquals(7, bitMatrix.getRowCount());
    }

    @Test
    public void getColumnCount() throws Exception {
        assertEquals(6, mapMatrix.getColumnCount());
        assertEquals(6, arrayMatrix.getColumnCount());
        assertEquals(6, bitMatrix.getColumnCount());
    }

    @Test
    public void getEffectiveCount() throws Exception {
        assertEquals(3, mapMatrix.getEffectiveCount());
        assertEquals(3, arrayMatrix.getEffectiveCount());
        assertEquals(42, bitMatrix.getEffectiveCount());
    }

    @Test
    public void getResolution() throws Exception {
        assertEquals(1, mapMatrix.getResolution());
        assertEquals(1, arrayMatrix.getResolution());
        assertEquals(1, bitMatrix.getResolution());
    }

    @Test
    public void get() throws Exception {
        assertEquals("r0_c1", mapMatrix.get(0, 1));
        assertEquals("r0_c1", arrayMatrix.get(0, 1));
        assertEquals("r0_c1", mapMatrix.get(1));
        assertEquals("r0_c1", arrayMatrix.get(1));
        assertTrue(bitMatrix.get(0, 1));
    }

    @Test
    public void set() throws Exception {
        mapMatrix.set(3, 3, "r3_c3");
        assertEquals("r3_c3", mapMatrix.get(3, 3));

        arrayMatrix.set(3, 3, "r3_c3");
        assertEquals("r3_c3", arrayMatrix.get(3, 3));

        bitMatrix.set(3, 3, true);
        assertEquals(true, bitMatrix.get(3, 3));
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(!mapMatrix.isEmpty());
        assertTrue(!arrayMatrix.isEmpty());
        assertTrue(!bitMatrix.isEmpty());
    }

    @Test
    public void iterator() throws Exception {
        int num = 0;
        Iterator<String> iterator = mapMatrix.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            assertNotNull(s);
            num++;
        }
        assertEquals(mapMatrix.getEffectiveCount(), num);

        num = 0;
        iterator = arrayMatrix.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            assertNotNull(s);
            num++;
        }
        assertEquals(arrayMatrix.getEffectiveCount(), num);

        num = 0;
        Iterator<Boolean> bitMatrixIterator = bitMatrix.iterator();
        while (bitMatrixIterator.hasNext()) {
            Boolean s = bitMatrixIterator.next();
            assertNotNull(s);
            num++;
        }
        assertEquals(bitMatrix.getEffectiveCount(), num);
    }

    @Test
    public void keyIterator() throws Exception {
        int num = 0;
        Iterator<Integer> keyIterator = mapMatrix.keyIterator();
        while (keyIterator.hasNext()) {
            Integer flatIndex = keyIterator.next();
            String s = mapMatrix.get(flatIndex);
            assertNotNull(s);
            num++;
        }
        assertEquals(mapMatrix.getEffectiveCount(), num);

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
        assertEquals(0, mapMatrix.getStartRow(arrayMatrix));
        assertEquals(0, arrayMatrix.getStartRow(mapMatrix));
        assertEquals(4, bitMatrix.getStartRow(mapMatrix));
    }

    @Test
    public void getStartColumn() throws Exception {
        assertEquals(0, mapMatrix.getStartColumn(arrayMatrix));
        assertEquals(0, arrayMatrix.getStartColumn(mapMatrix));
        assertEquals(3, bitMatrix.getStartColumn(mapMatrix));
    }

    @Test
    public void crop() throws Exception {
        int effectiveCount = mapMatrix.getEffectiveCount();
        int rowCount = mapMatrix.getRowCount();
        int columnCount = mapMatrix.getColumnCount();
        mapMatrix.crop();
        assertEquals(effectiveCount, mapMatrix.getEffectiveCount());
        assert rowCount >= mapMatrix.getRowCount();
        assert columnCount >= mapMatrix.getColumnCount();

        effectiveCount = arrayMatrix.getEffectiveCount();
        rowCount = arrayMatrix.getRowCount();
        columnCount = arrayMatrix.getColumnCount();
        arrayMatrix.crop();
        assertEquals(effectiveCount, arrayMatrix.getEffectiveCount());
        assert rowCount >= arrayMatrix.getRowCount();
        assert columnCount >= arrayMatrix.getColumnCount();
    }
}
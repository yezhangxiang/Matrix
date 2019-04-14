package com.yzx.matrix;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kele on 2019/4/13.
 */
public class MatrixTest {
    MapMatrix<String> mapMatrix;
    ArrayMatrix<String> arrayMatrix;
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
    }

    @Test
    public void getTopLeftX() throws Exception {
        assertEquals(0.0, mapMatrix.getTopLeftX(), 0.0);
        assertEquals(0.0, arrayMatrix.getTopLeftX(), 0.0);
    }

    @Test
    public void getTopLeftY() throws Exception {
        assertEquals(0.0, mapMatrix.getTopLeftY(), 0.0);
        assertEquals(0.0, arrayMatrix.getTopLeftY(), 0.0);
    }

    @Test
    public void getBottomRightX() throws Exception {
        assertEquals(6.0, mapMatrix.getBottomRightX(), 0.0);
        assertEquals(6.0, mapMatrix.getBottomRightX(), 0.0);
    }

    @Test
    public void getBottomRightY() throws Exception {
        assertEquals(-5.0, mapMatrix.getBottomRightY(), 0.0);
        assertEquals(-5.0, mapMatrix.getBottomRightY(), 0.0);
    }

    @Test
    public void getRowCount() throws Exception {
        assertEquals(5, mapMatrix.getRowCount());
        assertEquals(5, arrayMatrix.getRowCount());
    }

    @Test
    public void getColumnCount() throws Exception {
        assertEquals(6, mapMatrix.getColumnCount());
        assertEquals(6, arrayMatrix.getColumnCount());
    }

    @Test
    public void getEffectiveCount() throws Exception {
        assertEquals(3, mapMatrix.getEffectiveCount());
        assertEquals(3, arrayMatrix.getEffectiveCount());
    }

    @Test
    public void getResolution() throws Exception {
        assertEquals(1, mapMatrix.getResolution());
        assertEquals(1, arrayMatrix.getResolution());
    }

    @Test
    public void get() throws Exception {
        assertEquals("r0_c1", mapMatrix.get(0, 1));
        assertEquals("r0_c1", arrayMatrix.get(0, 1));
    }

    @Test
    public void set() throws Exception {
        mapMatrix.set(3, 3, "r3_c3");
        assertEquals("r3_c3", mapMatrix.get(3, 3));

        arrayMatrix.set(3, 3, "r3_c3");
        assertEquals("r3_c3", arrayMatrix.get(3, 3));
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(!mapMatrix.isEmpty());
        assertTrue(!arrayMatrix.isEmpty());
    }

    @Test
    public void iterator() throws Exception {

    }

    @Test
    public void keyIterator() throws Exception {

    }

    @Test
    public void getStartRow() throws Exception {

    }

    @Test
    public void getStartColumn() throws Exception {

    }

    @Test
    public void crop() throws Exception {

    }

}
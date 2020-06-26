package pers.yzx.geometry;

import org.junit.Assert;
import org.junit.Test;

public class VectorTest {

    @Test
    public void mag() {
        Vector vector = new Vector(1, 2, 3);
        Assert.assertEquals(3.7417, vector.mag(), 0.0001);
    }

    @Test
    public void mult() {
        Vector vector = new Vector(1, 2, 3);
        Vector multVector = vector.mult(1.5);
        Assert.assertEquals(1.5, multVector.x, 0.0001);
        Assert.assertEquals(3.0, multVector.y, 0.0001);
        Assert.assertEquals(4.5, multVector.z, 0.0001);
    }

    @Test
    public void dot() {
        Vector vector = new Vector(1, 2, 3);
        Vector vector1 = new Vector(1, 2, 3);
        double dot = vector.dot(vector1);
        Assert.assertEquals(14, dot, 0.00001);
    }

    @Test
    public void cross() {
        Vector vector = new Vector(1, 2, 3);
        Vector vector1 = new Vector(1, 2, 3);
        Vector cross = vector.cross(vector1);
        Assert.assertEquals(0, cross.x, 0.00001);
        Assert.assertEquals(0, cross.y, 0.00001);
        Assert.assertEquals(0, cross.z, 0.00001);
    }

    @Test
    public void pseudoCross() {
        Vector vector = new Vector(1, 1);
        Vector vector1 = new Vector(0, 1);
        double pseudoCross = vector.pseudoCross(vector1);
        Assert.assertEquals(1, pseudoCross, 0.00001);
    }

    @Test
    public void angle() {
        Vector vector = new Vector(1, 1);
        Vector vector1 = new Vector(0, 1);
        double angle = vector.angle(vector1);
        Assert.assertEquals(Math.PI / 4, angle, 0.00001);
        vector1 = new Vector(1, 1.2);
        angle = vector.angle(vector1) / Math.PI * 180;
        Assert.assertEquals(5.194428, angle, 0.00001);
    }
}
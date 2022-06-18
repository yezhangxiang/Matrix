package pers.yzx.geometry;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VectorTest {

    @Test
    public void broad() {
        int N = 3;
        int [][] matrix = new int[N][N];
        matrix[0][1] = 1;
        matrix[1][0] = 1;
        matrix[1][2] = 1;
        matrix[2][1] = 1;
        // List 就是 vector, null 就是空指针
        List<Set<Integer>> serverSetList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= i; j++) {
                if (matrix[i][j] != 0) {
                    Set<Integer> fooSet = null;
                    Set<Integer> barSet = null;
                    // 遍历已经找到的服务器集合（所有能相连接的服务器定义为一个集合）
                    for (Set<Integer> serverSet : serverSetList) {
                        if (serverSet.contains(i)) {
                            fooSet = serverSet;
                        }
                        if (serverSet.contains(j)) {
                            barSet = serverSet;
                        }
                    }
                    if (fooSet == null && barSet == null) {
                        Set<Integer> newServerSet = new HashSet<>();
                        newServerSet.add(i);
                        newServerSet.add(j);
                        serverSetList.add(newServerSet);
                    }
                    if (fooSet == null && barSet != null) {
                        barSet.add(i);
                    }
                    if (fooSet != null && barSet == null) {
                        fooSet.add(j);
                    }
                    if (fooSet != null && barSet != null) {
                        fooSet.addAll(barSet);
                        serverSetList.remove(barSet);
                    }
                }
            }
        }
        System.out.println(serverSetList.size());
    }

    @Test
    public void maxNum() {
        long x = 6761;
        int y = 2;
        int z; // output
        long prefixNum = 1;
        for (int i = 0; i < y; i++) {
            prefixNum *= 26;
        }
        // 这段在做向上取整
        double suffixNum = (double)x/prefixNum;
        int suffixNumInt = (int)suffixNum;
        int realSuffixNum = suffixNumInt;
        if (suffixNum == suffixNumInt) {
            realSuffixNum = suffixNumInt - 1;
        }

        z = 1;
        while (realSuffixNum >= 10) {
            realSuffixNum /= 10;
            z++;
        }

        System.out.println("length is " + z);
    }

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
package com.yzx;

public class Main {
    public static void traverseInteger(int row, int column) {
        Integer[][] integers = new Integer[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                integers[i][j] = 0;
            }
        }
    }
    public static void traverseInt(int row, int column) {
        int[][] ints = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                ints[i][j] = 0;
            }
        }
    }

    public static void main(String[] args) {
	// write your code here
        int row = 10000;
        int column = 10000;
        int repeat = 10;

        long start = System.currentTimeMillis();
        for (int i = 0; i < repeat; i++) {
            traverseInteger(row, column);
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start)/1000);


        start = System.currentTimeMillis();
        for (int i = 0; i < repeat; i++) {
            traverseInt(row, column);
        }
        end = System.currentTimeMillis();
        System.out.println((end-start)/1000);


        Matrix<Integer> mapMatrix = new MapMatrix<>(1000, 1000, 20, 0, 0);
        Matrix<Integer> arrayMatrix = new ArrayMatrix<>(1000, 1000, 20, 0, 0);

    }
}

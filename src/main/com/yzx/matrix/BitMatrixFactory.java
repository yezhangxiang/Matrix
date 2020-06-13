package com.yzx.matrix;

import com.yzx.geometry.Point;
import com.yzx.geometry.Polygon;

import java.util.*;

public class BitMatrixFactory {

    public static BitMatrix createByScanLine(Polygon polygon, int resolution) {
        Bound bound = polygon.getBound(resolution);
        BitMatrix bitMatrix = new BitMatrix(bound);

        Map<Integer, List<Edge>> edgeTable = createEdgeTable(polygon, bitMatrix);
        List<Edge> activeEdgeTable = new ArrayList<>();
        for (int i = 0; i < bitMatrix.getRowCount(); i++) {
            insertNetListToAet(edgeTable.get(i), activeEdgeTable);
            fillAetScanLine(activeEdgeTable, i, bitMatrix);
            removeNonActiveEdgeFromAet(activeEdgeTable, bitMatrix.getTopLeftY() - (i + 1) * bitMatrix.getResolution());
            updateAndResortAet(activeEdgeTable);
        }

        return bitMatrix;
    }

    private static void updateAndResortAet(List<Edge> activeEdgeTable) {
        for (Edge edge : activeEdgeTable) {
            edge.x += edge.dx;
        }
    }

    private static void removeNonActiveEdgeFromAet(List<Edge> activeEdgeTable, double nextLineY) {
        Iterator<Edge> iterator = activeEdgeTable.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.yMin >= nextLineY) {
                iterator.remove();
            }
        }
    }

    private static void fillAetScanLine(List<Edge> activeEdgeTable, int i, BitMatrix bitMatrix) {
        for (int j = 0; j < activeEdgeTable.size(); j += 2) {
            double xIn = activeEdgeTable.get(j).x;
            double xOut = activeEdgeTable.get(j + 1).x;
            int indexIn = (int) ((xIn - bitMatrix.getTopLeftX()) / bitMatrix.getResolution());
            int indexOut = (int) Math.ceil((xOut - bitMatrix.getTopLeftX()) / bitMatrix.getResolution());
            for (int k = indexIn; k < indexOut; k++) {
                bitMatrix.set(i, k, true);
            }
        }
    }

    private static void insertNetListToAet(List<Edge> edges, List<Edge> activeEdgeTable) {
        if (null == edges) {
            return;
        }
        for (Edge edge : edges) {
            boolean isAdd = false;
            for (int i = 0; i < activeEdgeTable.size(); i++) {
                Edge edgeNext = activeEdgeTable.get(i);
                if (edge.x > edgeNext.x) {
                    continue;
                }
                if (edge.x == edgeNext.x && edge.dx > edgeNext.dx) {
                    continue;
                }
                activeEdgeTable.add(i, edge);
                isAdd = true;
                break;
            }
            if (!isAdd) {
                activeEdgeTable.add(edge);
            }
        }
    }

    private static Map<Integer, List<Edge>> createEdgeTable(Polygon polygon, BitMatrix bitMatrix) {
        List<Point> points = polygon.getPoints();
        Map<Integer, List<Edge>> edgeTable = new HashMap<>();
        int resolution = bitMatrix.getResolution();

        for (int i = 0; i < points.size(); i++) {
            double x1 = points.get(i).getX();
            double x2 = points.get((i + 1) % points.size()).getX();
            double y1 = points.get(i).getY();
            double y2 = points.get((i + 1) % points.size()).getY();

            double yMin = y1 > y2 ? y2 : y1;
            double yMax = y1 > y2 ? y1 : y2;
            double slope = -(x1 - x2) / (y1 - y2);
            int yMaxRowIndex = (int) Math.ceil(bitMatrix.getTopLeftY() - yMax) / resolution;
            double yRow = bitMatrix.getTopLeftY() - yMaxRowIndex * resolution;
            // 水平线直接舍弃
            if (yRow <= y1 && y1 < yRow + resolution && yRow <= y2 && y2 < yRow + resolution) {
                continue;
            }
            double dx = slope * resolution;
            double x = x1 + slope * (y1 - yRow);
            // 创立新边,插入边表
            Edge edge = new Edge(yMin, x, dx);
            List<Edge> edgeList = edgeTable.get(yMaxRowIndex);
            if (null == edgeList) {
                edgeList = new ArrayList<>();
                edgeTable.put(yMaxRowIndex, edgeList);
            }
            edgeList.add(edge);
        }
        return edgeTable;
    }

    static class Edge {
        double yMin;
        double x;
        double dx;

        Edge(double yMin, double x, double dx) {
            this.yMin = yMin;
            this.x = x;
            this.dx = dx;
        }
    }
}

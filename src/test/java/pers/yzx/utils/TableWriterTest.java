package pers.yzx.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TableWriterTest {
    @Test
    public void write() throws Exception {
        List<Cell> cells = IntStream.range(0, 10).mapToObj(i -> new Cell(i, "cell_" + i)).collect(Collectors.toList());
        Map<String, Function<Cell, String>> functionMap = new HashMap<>();
        functionMap.put("name", Cell::getName);
        functionMap.put("id", cell -> String.valueOf(cell.getId()));

        TableWriter<Cell> tableWriter = new TableWriter<>(functionMap, cells);
        System.out.println(tableWriter.write());
    }

}

class Cell {
    private final int id;
    private final String name;

    Cell(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

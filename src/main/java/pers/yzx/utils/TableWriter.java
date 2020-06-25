package pers.yzx.utils;

import java.util.*;
import java.util.function.Function;

public class TableWriter<T> {
    private final Map<String, Function<T, String>> functionMap;
    private final List<T> tList;
    private final CharSequence delimiter;

    public TableWriter(Map<String, Function<T, String>> functionMap, List<T> tList) {
        this.functionMap = functionMap;
        this.tList = tList;
        this.delimiter = ",";
    }

    public String write() {
        List<String> keys = new ArrayList<>(functionMap.keySet());
        StringBuilder sb = new StringBuilder(String.join(delimiter, keys));
        sb.append(System.lineSeparator());
        for (T t : tList) {
            List<String> items = new ArrayList<>();
            for (String key : keys) {
                Function<T, String> stringFunction = functionMap.get(key);
                String s = stringFunction.apply(t);
                items.add(s);
            }
            sb.append(String.join(delimiter, items)).append(System.lineSeparator());
        }
        return sb.toString();
    }
}

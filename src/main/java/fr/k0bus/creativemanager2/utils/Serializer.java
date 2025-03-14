package fr.k0bus.creativemanager2.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Serializer {

    private static final int MAX_ARGS = 2;

    public static int[] readIntArray(List<?> objectList) {
        List<Integer> values = new ArrayList<>();
        for (Object o : objectList) {
            if (o instanceof Integer) {
                values.add((int) o);
            } else if (o instanceof String) {
                String[] args = ((String) o).split("-");
                if (args.length >= MAX_ARGS) {
                    int n0 = Integer.parseInt(args[0]);
                    int n1 = Integer.parseInt(args[1]) + 1;
                    for (int n : IntStream.range(n0, n1).toArray()) {
                        values.add(n);
                    }
                }
            }
        }
        return values.stream().mapToInt(Integer::intValue).toArray();
    }
}

package fr.k0bus.creativemanager2.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GuiUtils {

    private static final int MAX_ARGS = 2;

    @SuppressFBWarnings("BX_UNBOXING_IMMEDIATELY_REBOXED")
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

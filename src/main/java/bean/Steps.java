package bean;

import java.util.HashMap;

public class Steps {

    private static final HashMap<Long, String> steps = new HashMap<>();

    public static String get(Long key) {
        return steps.getOrDefault(key, "main");
    }

    public static void set(Long key, String step) {
        steps.put(key, step);
    }
}

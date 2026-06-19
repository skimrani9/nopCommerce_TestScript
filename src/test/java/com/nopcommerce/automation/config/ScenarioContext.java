package com.nopcommerce.automation.config;

import java.util.HashMap;
import java.util.Map;

public final class ScenarioContext {

    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

    private ScenarioContext() {
    }

    public static void set(String key, Object value) {
        context.get().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) context.get().get(key);
    }

    public static void clear() {
        context.get().clear();
        context.remove();
    }
}

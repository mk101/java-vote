package org.example.common.context;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private final Map<String, Object> beans = new HashMap<>();
    private static ApplicationContext instance = null;

    protected ApplicationContext() {}

    public void addBean(String name, Object value) {
        beans.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        if (!beans.containsKey(name)) {
            return null;
        }

        return (T) beans.get(name);
    }

    public static ApplicationContext get() {
        if (instance == null) {
            instance = new ApplicationContext();
        }

        return instance;
    }

}

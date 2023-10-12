package org.example.common.context;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class ApplicationContext {

    private static final String PROPERTIES_FILENAME = "application.properties";

    private final Map<String, Object> beans = new HashMap<>();
    private final Properties properties = new Properties();

    private static ApplicationContext instance = null;

    protected ApplicationContext() {
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILENAME));
        } catch (IOException | NullPointerException e) {
            log.error("Can't load properties", e);
            System.exit(-1);
        }
    }

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

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public static ApplicationContext get() {
        if (instance == null) {
            instance = new ApplicationContext();
        }

        return instance;
    }

}

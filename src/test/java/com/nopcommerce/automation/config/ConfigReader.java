package com.nopcommerce.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private ConfigReader() {
    }

    private static void loadProperties() {
        String environment = System.getProperty("environment", "local");
        String configFile = "config/" + environment + ".properties";

        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(configFile)) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file not found: " + configFile);
            }
            properties.load(inputStream);
            logger.info("Loaded configuration from {}", configFile);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load configuration file: " + configFile, exception);
        }
    }

    public static String getProperty(String key) {
        String envValue = System.getenv(toEnvKey(key));
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }

        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return trimTrailingSlash(getProperty("base.url"));
    }

    public static String getAdminEmail() {
        return getProperty("admin.email");
    }

    public static String getAdminPassword() {
        return getProperty("admin.password");
    }

    public static String getCustomerEmail() {
        return getProperty("customer.email");
    }

    public static String getCustomerPassword() {
        return getProperty("customer.password");
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static int getExplicitWaitSeconds() {
        return Integer.parseInt(getProperty("explicit.wait.seconds"));
    }

    private static String trimTrailingSlash(String url) {
        if (url != null && url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    private static String toEnvKey(String propertyKey) {
        return propertyKey.replace('.', '_').toUpperCase();
    }
}

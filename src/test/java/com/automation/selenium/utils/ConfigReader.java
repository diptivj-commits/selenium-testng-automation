package com.automation.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = loadProperties();

    private ConfigReader() {
        // Prevent creating instances of this utility class
    }

    private static Properties loadProperties() {
        Properties config = new Properties();

        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                    "config.properties not found. "
                    + "Place it in src/test/resources."
                );
            }

            config.load(input);
            return config;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to read config.properties", e
            );
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing or empty configuration property: " + key
            );
        }

        return value.trim();
    }

    public static int getPositiveInt(String key) {
        String value = getProperty(key);

        try {
            int number = Integer.parseInt(value);

            if (number <= 0) {
                throw new IllegalArgumentException(
                        key + " must be greater than zero"
                );
            }

            return number;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    key + " must be a whole number: " + value, e
            );
        }
    }
}

package com.insider.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        try {
            FileInputStream file = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(file);
            logger.info("Successfully loaded configuration from: {}.", CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Critical: Failed to load the configuration file from {}. Please ensure 'config.properties' exists and is accessible.",
                    CONFIG_FILE_PATH, e);
            throw new RuntimeException("Application cannot proceed: Failed to load config.properties. " + e.getMessage(), e);
        }
    }
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Configuration property '{}' was not found in config.properties. Please check your configuration.", key);
        }
        return value;
    }
    public static int getIntProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Mandatory integer property '" + key + "' is missing from config.properties.");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Property '" + key + "' must be an integer, but found: '" + value + "'. Please ensure the value is a valid number.", e);
        }
    }
}
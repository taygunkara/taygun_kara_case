package com.insider.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonDataReader {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataReader.class);

    public static List<Map<String, String>> readJsonData(String filePath) {
        logger.info("Attempting to read JSON test data from file: {}", filePath);
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(filePath);
            List<Map<String, String>> data = mapper.readValue(file, new TypeReference<List<Map<String, String>>>() {});
            logger.info("Successfully read {} entries from JSON file: {}", data.size(), filePath);
            return data;
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}. Error: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Cannot read test data from JSON: " + filePath, e);
        }
    }
}

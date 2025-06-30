package com.hainam.judgeql.shared.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convert object to JSON string
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Error converting to JSON: {}", e.getMessage());
            throw new RuntimeException("Error converting to JSON", e);
        }
    }

    /**
     * Convert JSON string to Map
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            logger.error("Error converting from JSON to Map: {}", e.getMessage());
            throw new RuntimeException("Error converting from JSON to Map", e);
        }
    }

    /**
     * Convert JSON string to JsonNode
     */
    public static JsonNode fromJsonToJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            logger.error("Error converting from JSON to JsonNode: {}", e.getMessage());
            throw new RuntimeException("Error converting from JSON to JsonNode", e);
        }
    }

    /**
     * Convert JSON string to specified class
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("Error converting from JSON to {}: {}", clazz.getSimpleName(), e.getMessage());
            throw new RuntimeException("Error converting from JSON to " + clazz.getSimpleName(), e);
        }
    }
}

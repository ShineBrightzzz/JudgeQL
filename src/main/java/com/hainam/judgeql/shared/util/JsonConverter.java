package com.hainam.judgeql.shared.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Converter for handling JSONB data type in PostgreSQL.
 * This converter allows JPA entities to use Object or Map fields
 * that will be stored as JSONB in the database.
 */
@Converter
public class JsonConverter implements AttributeConverter<Object, String> {
    private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.error("Error converting to JSON: {}", e.getMessage());
            throw new RuntimeException("Error converting to JSON", e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        try {
            // Try to parse as List of Maps first (for array structures)
            return objectMapper.readValue(dbData, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            try {
                // Try to parse as generic Map (for object structures)
                return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
            } catch (IOException ex) {
                try {
                    // If that fails, return as Object
                    return objectMapper.readValue(dbData, Object.class);
                } catch (JsonProcessingException jpe) {
                    logger.error("Error converting from JSON: {}", jpe.getMessage());
                    throw new RuntimeException("Error converting from JSON", jpe);
                }
            }
        }
    }
}

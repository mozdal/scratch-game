package org.scratchgame.infrastructure.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    private final ObjectMapper objectMapper;

    public ConfigLoader() {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public GameConfig loadConfig(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), GameConfig.class);
    }
}

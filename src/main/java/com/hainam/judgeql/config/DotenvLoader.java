package com.hainam.judgeql.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to load environment variables from a .env file
 */
public class DotenvLoader {

    /**
     * Load environment variables from .env file
     */
    public static void load() {
        File envFile = new File(".env");
        if (envFile.exists()) {
            try {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(envFile)) {
                    props.load(fis);
                }
                
                props.forEach((key, value) -> {
                    if (!System.getProperties().containsKey(key.toString())) {
                        System.setProperty(key.toString(), value.toString());
                    }
                });
                
                System.out.println("Loaded environment variables from .env file");
            } catch (IOException e) {
                System.err.println("Error loading .env file: " + e.getMessage());
            }
        } else {
            System.out.println(".env file not found at: " + envFile.getAbsolutePath());
        }
    }
}

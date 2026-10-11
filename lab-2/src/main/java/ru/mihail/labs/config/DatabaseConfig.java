package ru.mihail.labs.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private final String url;
    private final String username;
    private final String password;
    private final String schema;

    public DatabaseConfig(String url, String username, String password, String schema) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.schema = schema;
    }

    public static DatabaseConfig load(String resourceName) {
        Properties properties = new Properties();

        try (InputStream inputStream = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Configuration file not found: " + resourceName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read configuration: " + resourceName, e);
        }

        return new DatabaseConfig(
                requiredProperty(properties, "database.url"),
                requiredProperty(properties, "database.username"),
                requiredProperty(properties, "database.password"),
                requiredProperty(properties, "database.schema")
        );
    }

    private static String requiredProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required property is missing: " + key);
        }
        return value;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }
}

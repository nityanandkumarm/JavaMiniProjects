package com.builder.database.util;

import java.util.Set;

public class PostgresTypeValidator {
    private static final Set<String> ALLOWED_TYPES = Set.of(
        "UUID", "VARCHAR", "TEXT", "INT", "INTEGER", "BIGINT", "BOOLEAN", "TIMESTAMP", "DATE", "NUMERIC", "DECIMAL"
    );

    private PostgresTypeValidator() {
        throw new IllegalStateException("Utility class");
    }


    public static boolean isValidType(String type) {
        // Basic form check: "VARCHAR(255)" → "VARCHAR"
        String baseType = type.toUpperCase().split("\\(")[0].trim();
        return ALLOWED_TYPES.contains(baseType);
    }

    public static void validateType(String type) {
        if (!isValidType(type)) {
            throw new IllegalArgumentException("Unsupported PostgreSQL type: " + type);
        }
    }
}

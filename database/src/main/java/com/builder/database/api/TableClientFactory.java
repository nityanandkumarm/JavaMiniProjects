package com.builder.database.api;

public class TableClientFactory {

    private static TableManager instance;

    private TableClientFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static TableManager get() {
        if (instance == null) {
            throw new IllegalStateException("No TableManager implementation registered.");
        }
        return instance;
    }

    public static void register(TableManager manager) {
        instance = manager;
    }
}

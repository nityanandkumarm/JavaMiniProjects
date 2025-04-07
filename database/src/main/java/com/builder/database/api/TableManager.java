package com.builder.database.api;

import com.builder.database.model.TableDefinitionRequest;

public interface TableManager {
    void createTable(TableDefinitionRequest request);
}

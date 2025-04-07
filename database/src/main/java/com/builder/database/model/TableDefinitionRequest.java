package com.builder.database.model;

import com.builder.database.util.PostgresTypeValidator;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TableDefinitionRequest {
    private final String schemaName;
    private final String tableName;
    private final List<ColumnDefinition> columns;

    @Builder(builderMethodName = "builder")
    private TableDefinitionRequest(String schemaName, String tableName, List<ColumnDefinition> columns) {
        this.schemaName = schemaName == null ? "public" : schemaName;
        this.tableName = tableName;
        this.columns = columns == null ? new ArrayList<>() : columns;
    }

    public static class TableDefinitionRequestBuilder {
        private List<ColumnDefinition> columns = new ArrayList<>();

        public TableDefinitionRequestBuilder addColumn(String name, String type, boolean primaryKey, boolean notNull, String defaultValue) {
            PostgresTypeValidator.validateType(type);
            this.columns.add(ColumnDefinition.builder()
                    .name(name)
                    .type(type)
                    .primaryKey(primaryKey)
                    .notNull(notNull)
                    .defaultValue(defaultValue)
                    .build());
            return this;
        }
    }
}

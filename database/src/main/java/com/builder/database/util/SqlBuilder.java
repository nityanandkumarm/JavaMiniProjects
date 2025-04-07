package com.builder.database.util;

import com.builder.database.model.ColumnDefinition;
import com.builder.database.model.TableDefinitionRequest;

import java.util.List;
import java.util.stream.Collectors;

public class SqlBuilder {

    private SqlBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static String buildActualTableSql(TableDefinitionRequest request) {
        String fullTableName = request.getSchemaName() + "." + request.getTableName();
        List<String> columnDefs = request.getColumns().stream()
                .map(SqlBuilder::buildColumnDefinition)
                .collect(Collectors.toList());

        columnDefs.add("lastUpdated TIMESTAMP DEFAULT now()");
        columnDefs.add("isDeleted BOOLEAN DEFAULT false");

        return "CREATE TABLE IF NOT EXISTS " + fullTableName + " (\n" +
                String.join(",\n", columnDefs) + "\n);";
    }

    public static String buildTempTableSql(TableDefinitionRequest request) {
        String fullTempName = request.getSchemaName() + "._tmp_" + request.getTableName();
        List<String> columnDefs = request.getColumns().stream()
                .map(col -> col.getName() + " TEXT")
                .collect(Collectors.toList());

        columnDefs.add("lastUpdateDate TEXT");
        columnDefs.add("isDeleted TEXT");

        return "CREATE TABLE IF NOT EXISTS " + fullTempName + " (\n" +
                String.join(",\n", columnDefs) + "\n);";
    }

    private static String buildColumnDefinition(ColumnDefinition col) {
        StringBuilder sb = new StringBuilder(col.getName()).append(" ").append(col.getType());
        if (col.isNotNull()) sb.append(" NOT NULL");
        if (col.getDefaultValue() != null) sb.append(" DEFAULT ").append(col.getDefaultValue());
        if (col.isPrimaryKey()) sb.append(" PRIMARY KEY"); // assumes single PK for now
        return sb.toString();
    }
}

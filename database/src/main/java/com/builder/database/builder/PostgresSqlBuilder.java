package com.builder.database.builder;

import com.builder.database.model.ColumnDefinition;
import com.builder.database.model.TableDefinitionRequest;

import java.util.List;
import java.util.stream.Collectors;

public class PostgresSqlBuilder implements SqlBuilder {

    @Override
    public String buildCreateTableSql(TableDefinitionRequest request) {
        String fullTableName = quote(request.getSchemaName()) + "." + quote(request.getTableName());

        List<String> columnDefs = request.getColumns().stream()
            .map(this::buildActualColumnDefinition)
            .collect(Collectors.toList());

        columnDefs.add(quote("lastUpdateDate") + " TIMESTAMP NOT NULL");
        columnDefs.add(quote("isDeleted") + " BOOLEAN NOT NULL DEFAULT FALSE");

        return "CREATE TABLE IF NOT EXISTS " + fullTableName + " (\n" +
               String.join(",\n", columnDefs) + "\n" +
               ");";
    }

    @Override
    public String buildCreateTempWriteTableSql(TableDefinitionRequest request) {
        final String COLUMN_DATA_TYPE = "TEXT";
        String tempTableName = quote(request.getSchemaName()) + "." +
                quote("__tmp_write_" + request.getTableName());

        List<String> columnDefs = request.getColumns().stream()
            .map(col -> quote(col.getName()) + COLUMN_DATA_TYPE)
            .collect(Collectors.toList());

        columnDefs.add(quote("lastUpdateDate") + COLUMN_DATA_TYPE);
        columnDefs.add(quote("isDeleted") + COLUMN_DATA_TYPE);

        return "CREATE TABLE IF NOT EXISTS " + tempTableName + " (\n" +
               String.join(",\n", columnDefs) + "\n" +
               ");";
    }

    private String buildActualColumnDefinition(ColumnDefinition col) {
        StringBuilder sb = new StringBuilder();
        sb.append(quote(col.getName())).append(" ").append(col.getType());
        if (col.isNotNull()) sb.append(" NOT NULL");
        if (col.isPrimaryKey()) sb.append(" PRIMARY KEY");
        if (col.getDefaultValue() != null && !col.getDefaultValue().isEmpty()) {
            sb.append(" DEFAULT ").append(col.getDefaultValue());
        }
        return sb.toString();
    }

    private String quote(String name) {
        return "\"" + name.replace("\"", "\"\"") + "\"";
    }
}

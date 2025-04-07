package com.builder.database.service;

import com.builder.database.api.TableManager;
import com.builder.database.model.ColumnDefinition;
import com.builder.database.model.TableDefinitionRequest;
import com.builder.database.util.PostgresTypeValidator;
import com.builder.database.util.SqlBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableService implements TableManager {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void ensureRegistryTable() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS table_registry (
                id SERIAL PRIMARY KEY,
                schema_name TEXT NOT NULL,
                table_name TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT now(),
                last_modified TIMESTAMP DEFAULT now()
            )
        """);
    }

    @Override
    public void createTable(TableDefinitionRequest request) {
        validateNames(request);

        String actualSql = SqlBuilder.buildActualTableSql(request);
        String tempSql = SqlBuilder.buildTempTableSql(request);

        jdbcTemplate.execute(actualSql);
        jdbcTemplate.execute(tempSql);

        jdbcTemplate.update("""
            INSERT INTO table_registry (schema_name, table_name)
            VALUES (?, ?)
        """, request.getSchemaName(), request.getTableName());
    }

    private void validateNames(TableDefinitionRequest request) {
        if (!request.getTableName().matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("Invalid table name.");
        }
        for (ColumnDefinition col : request.getColumns()) {
            if (!col.getName().matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
                throw new IllegalArgumentException("Invalid column name: " + col.getName());
            }
            PostgresTypeValidator.validateType(col.getType());
        }
    }
}

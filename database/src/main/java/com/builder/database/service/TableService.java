package com.builder.database.service;

import com.builder.database.builder.SqlBuilder;
import com.builder.database.builder.SqlBuilderFactory;
import com.builder.database.model.TableDefinitionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableService {

    private final JdbcTemplate jdbcTemplate;
    private final SqlBuilderFactory sqlBuilderFactory;

    public void createTable(TableDefinitionRequest request) {
        SqlBuilder sqlBuilder = sqlBuilderFactory.getBuilder();

        String createTableSql = sqlBuilder.buildCreateTableSql(request);
        jdbcTemplate.execute(createTableSql);

        String createTempTableSql = sqlBuilder.buildCreateTempWriteTableSql(request);
        jdbcTemplate.execute(createTempTableSql);
    }
}

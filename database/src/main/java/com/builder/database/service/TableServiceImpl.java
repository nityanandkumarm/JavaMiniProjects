package com.builder.database.service;

import com.builder.database.builder.SqlBuilder;
import com.builder.database.builder.SqlBuilderFactory;
import com.builder.database.config.FlushProperties;
import com.builder.database.dto.GenericResultRowDto;
import com.builder.database.dto.IndexDefinitionDto;
import com.builder.database.dto.SelectQueryRequestDto;
import com.builder.database.dto.TableCreateRequestDto;
import com.builder.database.mapper.TableMapper;
import com.builder.database.model.IndexDefinition;
import com.builder.database.model.TableDefinitionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final JdbcTemplate jdbcTemplate;
    private final SqlBuilderFactory sqlBuilderFactory;
    private final TableMapper tableMapper;
    private final FlushProperties flushProperties;
    private final TableMetadataService tableMetadataService;

    @Override
    public void createTable(TableCreateRequestDto requestDto) {
        SqlBuilder sqlBuilder = sqlBuilderFactory.getBuilder();

        TableDefinitionRequest request = tableMapper.toModel(requestDto);

        if (request.isTemporaryWriteTable()) {
            String createTempTableSql = sqlBuilder.buildCreateTempWriteTableSql(request);
            jdbcTemplate.execute(createTempTableSql);
            String createTableSql = sqlBuilder.buildCreateTableSql(request);
            jdbcTemplate.execute(createTableSql);

            sqlBuilder.buildAllCreateIndexSql(
                    request.getSchemaName(), request.getTableName(), request.getIndexes()
            ).forEach(jdbcTemplate::execute);
        } else {
            String createTableSql = sqlBuilder.buildCreateTableSql(request);
            jdbcTemplate.execute(createTableSql);

            sqlBuilder.buildAllCreateIndexSql(
                    request.getSchemaName(), request.getTableName(), request.getIndexes()
            ).forEach(jdbcTemplate::execute);
        }
    }

    @Override
    public List<GenericResultRowDto> executeSelectQuery(SelectQueryRequestDto requestDto) {
        SqlBuilder sqlBuilder = sqlBuilderFactory.getBuilder();

        String sql = sqlBuilder.buildSelectQuerySql(
                requestDto.getSchemaName(),
                requestDto.getTableName(),
                requestDto.getColumns(),
                requestDto.getFilters(),
                tableMapper.toModelAggregations(requestDto.getAggregations())
        );

        log.debug("Executing select: {}", sql);

        List<Map<String, Object>> rows = jdbcTemplate.query(sql, new ColumnMapRowMapper());

        return rows.stream()
                .map(tableMapper::fromMap)
                .toList();
    }

    @Override
    public void createIndex(String schemaName, String tableName, IndexDefinitionDto index) {
        SqlBuilder sqlBuilder = sqlBuilderFactory.getBuilder();
        IndexDefinition indexModel = tableMapper.toModel(index);
        String sql = sqlBuilder.buildCreateIndexSql(schemaName, tableName, indexModel);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void flushTempToActual(String schema, String table) {
        SqlBuilder sqlBuilder = sqlBuilderFactory.getBuilder();

        TableDefinitionRequest def = tableMetadataService.getTableDefinition(schema, table, false);
        int batchSize = flushProperties.getBatchSize();

        String sql = sqlBuilder.buildFlushFromTempToActualSql(def, batchSize);

        log.info("Flushing {} rows from {}.{} to actual table.", batchSize, schema, table);
        jdbcTemplate.execute(sql);
    }

}

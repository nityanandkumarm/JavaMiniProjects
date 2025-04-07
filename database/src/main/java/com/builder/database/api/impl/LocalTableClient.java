package com.builder.database.api.impl;

import com.builder.database.api.TableClient;
import com.builder.database.dto.*;
import com.builder.database.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LocalTableClient implements TableClient {

    private final TableService tableService;

    @Override
    public void createTable(TableCreateRequestDto requestDto) {
        tableService.createTable(requestDto);
    }

    @Override
    public void createIndex(String schemaName, String tableName, IndexDefinitionDto indexDto) {
        tableService.createIndex(schemaName, tableName, indexDto);
    }

    @Override
    public List<GenericResultRowDto> selectQuery(SelectQueryRequestDto requestDto) {
        return tableService.executeSelectQuery(requestDto);
    }
}

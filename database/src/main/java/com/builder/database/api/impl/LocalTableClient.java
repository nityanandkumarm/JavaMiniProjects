package com.builder.database.api.impl;

import com.builder.database.api.TableClient;
import com.builder.database.dto.TableCreateRequestDto;
import com.builder.database.mapper.TableMapper;
import com.builder.database.service.TableService;

public class LocalTableClient implements TableClient {

    private final TableService tableService;

    public LocalTableClient(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public void createTable(TableCreateRequestDto request) {
        var domainRequest = TableMapper.fromDto(request);
        tableService.createTable(domainRequest);
    }
}

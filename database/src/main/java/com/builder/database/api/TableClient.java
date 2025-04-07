package com.builder.database.api;

import com.builder.database.dto.TableCreateRequestDto;

public interface TableClient {
    void createTable(TableCreateRequestDto request);
    // Future: void insertData(...);
    // Future: void flushTable(...);
}

package com.builder.database.mapper;

import com.builder.database.dto.ColumnDefinitionDto;
import com.builder.database.dto.TableCreateRequestDto;
import com.builder.database.model.ColumnDefinition;
import com.builder.database.model.TableDefinitionRequest;

import java.util.List;

public class TableMapper {

    private TableMapper(){
        throw new IllegalStateException("Utility class");
    }

    public static TableDefinitionRequest fromDto(TableCreateRequestDto dto) {
        List<ColumnDefinition> columnDefinitions = dto.getColumns().stream()
                .map(TableMapper::mapColumn)
                .toList();

        return TableDefinitionRequest.builder()
                .schemaName(dto.getSchemaName())
                .tableName(dto.getTableName())
                .columns(columnDefinitions)
                .build();
    }

    private static ColumnDefinition mapColumn(ColumnDefinitionDto dto) {
        return ColumnDefinition.builder()
                .name(dto.getName())
                .type(dto.getType())
                .primaryKey(dto.isPrimaryKey())
                .notNull(dto.isNotNull())
                .defaultValue(dto.getDefaultValue())
                .build(); // this will trigger model-level validation
    }
}

package com.builder.database.controller;

import com.builder.database.dto.GenericResultRowDto;
import com.builder.database.dto.IndexDefinitionDto;
import com.builder.database.dto.SelectQueryRequestDto;
import com.builder.database.dto.TableCreateRequestDto;
import com.builder.database.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping("/create")
    public ResponseEntity<String> createTable(@RequestBody @Valid TableCreateRequestDto dto) {
        tableService.createTable(dto);
        return ResponseEntity.ok("Table created successfully.");
    }

    @PostMapping("/select")
    public ResponseEntity<List<GenericResultRowDto>> executeSelect(@RequestBody @Valid SelectQueryRequestDto request) {
        List<GenericResultRowDto> results = tableService.executeSelectQuery(request);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/{schema}/{table}/indexes")
    public ResponseEntity<String> createIndex(
            @PathVariable String schema,
            @PathVariable String table,
            @RequestBody @Valid IndexDefinitionDto indexDto
    ) {
        tableService.createIndex(schema, table, indexDto);
        return ResponseEntity.ok("Index created successfully.");
    }


}

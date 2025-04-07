package com.builder.database.controller;

import com.builder.database.model.TableDefinitionRequest;
import com.builder.database.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping("/create")
    public ResponseEntity<String> createTable(@RequestBody TableDefinitionRequest request) {
        tableService.createTable(request);
        return ResponseEntity.ok("Table and temp table created successfully");
    }
}

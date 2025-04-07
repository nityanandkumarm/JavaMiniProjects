package com.builder.database.controller;

import com.builder.database.dto.TableCreateRequestDto;
import com.builder.database.mapper.TableMapper;
import com.builder.database.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseEntity<String> createTable(@RequestBody @Valid TableCreateRequestDto dto) {
        tableService.createTable(TableMapper.fromDto(dto));
        return ResponseEntity.ok("Table created successfully.");
    }
}

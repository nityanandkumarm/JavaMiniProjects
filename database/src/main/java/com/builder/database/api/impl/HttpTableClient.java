package com.builder.database.api.impl;

import com.builder.database.api.TableClient;
import com.builder.database.api.config.TableClientConfig;
import com.builder.database.dto.TableCreateRequestDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

public class HttpTableClient implements TableClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    public HttpTableClient(TableClientConfig config) {
        this.baseUrl = config.getBaseUrl();
    }

    @Override
    public void createTable(TableCreateRequestDto request) {
        HttpEntity<TableCreateRequestDto> entity = new HttpEntity<>(request);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/api/v1/tables", entity, Void.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create table via HTTP");
        }
    }
}

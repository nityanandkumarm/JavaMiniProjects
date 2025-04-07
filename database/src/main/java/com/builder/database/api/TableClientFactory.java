package com.builder.database.api;

import com.builder.database.api.impl.HttpTableClient;
import com.builder.database.api.impl.LocalTableClient;
import com.builder.database.service.TableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableClientFactory {

    private final TableService tableService;

    public TableClient createLocalClient() {
        return new LocalTableClient(tableService);
    }

    public TableClient createHttpClient(String baseUrl) {
        RestTemplate restTemplate = createSafeRestTemplate();
        return new HttpTableClient(restTemplate, baseUrl);
    }

    private RestTemplate createSafeRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add error handler for REST call failures
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {

        });

        return restTemplate;
    }
}

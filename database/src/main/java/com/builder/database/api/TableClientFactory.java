package com.builder.database.api;

import com.builder.database.api.config.TableClientConfig;
import com.builder.database.api.impl.HttpTableClient;
import com.builder.database.api.impl.LocalTableClient;
import com.builder.database.service.TableService;

public class TableClientFactory {

    private TableClientFactory(){
        throw new IllegalStateException("Utility class");
    }
    public static TableClient create(TableClientConfig config) {
        return switch (config.getMode()) {
            case HTTP -> new HttpTableClient(config);
            case LOCAL -> new LocalTableClient(config.getTableService());
        };
    }
}

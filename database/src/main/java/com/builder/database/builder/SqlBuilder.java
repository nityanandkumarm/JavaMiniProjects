package com.builder.database.builder;

import com.builder.database.model.TableDefinitionRequest;

public interface SqlBuilder {
    String buildCreateTableSql(TableDefinitionRequest request);
    String buildCreateTempWriteTableSql(TableDefinitionRequest request);
}

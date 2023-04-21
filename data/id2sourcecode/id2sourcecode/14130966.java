    public String replaceTemplateVariables(IDbDialect dialect, DataEventType dml, Trigger trigger, TriggerHistory history, String tablePrefix, Table metaData, String defaultCatalog, String defaultSchema, String ddl) {
        boolean resolveSchemaAndCatalogs = trigger.getSourceCatalogName() != null || trigger.getSourceSchemaName() != null;
        ddl = replace("targetTableName", getDefaultTargetTableName(trigger, history), ddl);
        ddl = replace("defaultSchema", resolveSchemaAndCatalogs && defaultSchema != null && defaultSchema.length() > 0 ? defaultSchema + "." : "", ddl);
        ddl = replace("defaultCatalog", resolveSchemaAndCatalogs && defaultCatalog != null && defaultCatalog.length() > 0 ? defaultCatalog + "." : "", ddl);
        ddl = replace("triggerName", dialect.getTriggerName(dml, triggerPrefix, dialect.getMaxTriggerNameLength(), trigger, history).toUpperCase(), ddl);
        ddl = replace("engineName", dialect.getEngineName(), ddl);
        ddl = replace("prefixName", tablePrefix, ddl);
        ddl = replace("targetGroupId", trigger.getTargetGroupId(), ddl);
        ddl = replace("channelName", trigger.getChannelId(), ddl);
        ddl = replace("triggerHistoryId", Integer.toString(history == null ? -1 : history.getTriggerHistoryId()), ddl);
        String triggerExpression = dialect.getTransactionTriggerExpression(defaultCatalog, defaultSchema, trigger);
        if (dialect.isTransactionIdOverrideSupported() && trigger.getTxIdExpression() != null) {
            triggerExpression = trigger.getTxIdExpression();
        }
        ddl = replace("txIdExpression", dialect.preProcessTriggerSqlClause(triggerExpression), ddl);
        ddl = replace("nodeSelectWhere", dialect.preProcessTriggerSqlClause(trigger.getNodeSelect()), ddl);
        ddl = replace("nodeSelectWhereEscaped", replace("'", "''", trigger.getNodeSelect()), ddl);
        ddl = replace("syncOnInsertCondition", dialect.preProcessTriggerSqlClause(trigger.getSyncOnInsertCondition()), ddl);
        ddl = replace("syncOnUpdateCondition", dialect.preProcessTriggerSqlClause(trigger.getSyncOnUpdateCondition()), ddl);
        ddl = replace("syncOnDeleteCondition", dialect.preProcessTriggerSqlClause(trigger.getSyncOnDeleteCondition()), ddl);
        String syncTriggersExpression = dialect.getSyncTriggersExpression();
        syncTriggersExpression = replace("defaultCatalog", resolveSchemaAndCatalogs && defaultCatalog != null && defaultCatalog.length() > 0 ? defaultCatalog + "." : "", syncTriggersExpression);
        syncTriggersExpression = replace("defaultSchema", resolveSchemaAndCatalogs && defaultSchema != null && defaultSchema.length() > 0 ? defaultSchema + "." : "", syncTriggersExpression);
        ddl = replace("syncOnIncomingBatchCondition", trigger.isSyncOnIncomingBatch() ? "1=1" : syncTriggersExpression, ddl);
        ddl = replace("origTableAlias", ORIG_TABLE_ALIAS, ddl);
        Column[] columns = trigger.orderColumnsForTable(metaData);
        ColumnString columnString = buildColumnString(ORIG_TABLE_ALIAS, newTriggerValue, newColumnPrefix, columns);
        ddl = replace("columns", columnString.columnString, ddl);
        ddl = replace("virtualOldNewTable", buildVirtualTableSql(dialect, oldColumnPrefix, newColumnPrefix, metaData.getColumns()), ddl);
        String oldColumnString = "null";
        if (trigger.isSyncColumnLevel()) {
            oldColumnString = buildColumnString(ORIG_TABLE_ALIAS, oldTriggerValue, oldColumnPrefix, columns).columnString;
        }
        ddl = replace("oldColumns", oldColumnString, ddl);
        ddl = eval(columnString.isBlobClob, "containsBlobClobColumns", ddl);
        ddl = replace("tableName", history == null ? trigger.getSourceTableName() : history.getSourceTableName(), ddl);
        ddl = replace("schemaName", (history == null ? (resolveSchemaAndCatalogs && trigger.getSourceSchemaName() != null ? trigger.getSourceSchemaName() + "." : "") : (resolveSchemaAndCatalogs && history.getSourceSchemaName() != null ? history.getSourceSchemaName() + "." : "")), ddl);
        columns = metaData.getPrimaryKeyColumns();
        oldColumnString = buildColumnString(ORIG_TABLE_ALIAS, oldTriggerValue, oldColumnPrefix, columns).columnString;
        ddl = replace("oldKeys", oldColumnString, ddl);
        ddl = replace("oldNewPrimaryKeyJoin", aliasedPrimaryKeyJoin(oldTriggerValue, newTriggerValue, columns), ddl);
        ddl = replace("tableNewPrimaryKeyJoin", aliasedPrimaryKeyJoin(ORIG_TABLE_ALIAS, newTriggerValue, columns), ddl);
        ddl = replace("primaryKeyWhereString", getPrimaryKeyWhereString(newTriggerValue, columns), ddl);
        ddl = replace("declareOldKeyVariables", buildKeyVariablesDeclare(columns, "old"), ddl);
        ddl = replace("declareNewKeyVariables", buildKeyVariablesDeclare(columns, "new"), ddl);
        ddl = replace("oldKeyNames", buildColumnNameString(oldTriggerValue, columns), ddl);
        ddl = replace("newKeyNames", buildColumnNameString(newTriggerValue, columns), ddl);
        ddl = replace("oldKeyVariables", buildKeyVariablesString(columns, "old"), ddl);
        ddl = replace("newKeyVariables", buildKeyVariablesString(columns, "new"), ddl);
        ddl = replace("varNewPrimaryKeyJoin", aliasedPrimaryKeyJoinVar(newTriggerValue, "new", columns), ddl);
        ddl = replace("varOldPrimaryKeyJoin", aliasedPrimaryKeyJoinVar(oldTriggerValue, "old", columns), ddl);
        ddl = replace("newTriggerValue", newTriggerValue, ddl);
        ddl = replace("oldTriggerValue", oldTriggerValue, ddl);
        ddl = replace("newColumnPrefix", newColumnPrefix, ddl);
        ddl = replace("oldColumnPrefix", oldColumnPrefix, ddl);
        switch(dml) {
            case DELETE:
                ddl = replace("curTriggerValue", oldTriggerValue, ddl);
                ddl = replace("curColumnPrefix", oldColumnPrefix, ddl);
                break;
            case INSERT:
            case UPDATE:
            default:
                ddl = replace("curTriggerValue", newTriggerValue, ddl);
                ddl = replace("curColumnPrefix", newColumnPrefix, ddl);
                break;
        }
        return ddl;
    }

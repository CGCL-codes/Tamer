    protected String replaceTemplateVariables(DataEventType dml, TriggerHistory history, String tablePrefix, Table metaData, String ddl, boolean supportsBigLobs, boolean concatColumns) {
        Trigger trigger = history.getTrigger();
        boolean resolveSchemaAndCatalogs = history.getSourceCatalogName() != null || history.getSourceSchemaName() != null;
        ddl = StringUtils.replaceTokens("targetTableName", metaData.getTableName(), ddl);
        ddl = StringUtils.replaceTokens("triggerName", history.getTriggerNameForDmlType(dml), ddl);
        ddl = StringUtils.replaceTokens("prefixName", tablePrefix, ddl);
        ddl = StringUtils.replaceTokens("channelName", history.getTrigger().getChannelId(), ddl);
        ddl = StringUtils.replaceTokens("triggerHistoryId", Integer.toString(history == null ? -1 : history.getTriggerHistoryId()), ddl);
        String triggerExpression = getTransactionTriggerExpression();
        if (isTransactionIdOverrideSupported() && !StringUtils.isBlank(trigger.getTxIdExpression())) {
            triggerExpression = trigger.getTxIdExpression();
        }
        ddl = StringUtils.replaceTokens("txIdExpression", preProcessTriggerSqlClause(triggerExpression), ddl);
        ddl = StringUtils.replaceTokens("externalSelect", (trigger.getExternalSelect() == null ? "null" : "(" + preProcessTriggerSqlClause(trigger.getExternalSelect()) + ")"), ddl);
        ddl = StringUtils.replaceTokens("syncOnInsertCondition", preProcessTriggerSqlClause(trigger.getSyncOnInsertCondition()), ddl);
        ddl = StringUtils.replaceTokens("syncOnUpdateCondition", preProcessTriggerSqlClause(trigger.getSyncOnUpdateCondition()), ddl);
        ddl = StringUtils.replaceTokens("syncOnDeleteCondition", preProcessTriggerSqlClause(trigger.getSyncOnDeleteCondition()), ddl);
        ddl = StringUtils.replaceTokens("dataHasChangedCondition", preProcessTriggerSqlClause(getDataHasChangedCondition()), ddl);
        String defaultCatalog = dbDialect.getDefaultCatalog();
        String defaultSchema = dbDialect.getDefaultSchema();
        String syncTriggersExpression = getSyncTriggersExpression();
        syncTriggersExpression = StringUtils.replaceTokens("defaultCatalog", resolveSchemaAndCatalogs && defaultCatalog != null && defaultCatalog.length() > 0 ? defaultCatalog + "." : "", syncTriggersExpression);
        syncTriggersExpression = StringUtils.replaceTokens("defaultSchema", resolveSchemaAndCatalogs && defaultSchema != null && defaultSchema.length() > 0 ? defaultSchema + "." : "", syncTriggersExpression);
        ddl = StringUtils.replaceTokens("syncOnIncomingBatchCondition", trigger.isSyncOnIncomingBatch() ? SqlConstants.ALWAYS_TRUE_CONDITION : syncTriggersExpression, ddl);
        ddl = StringUtils.replaceTokens("origTableAlias", ORIG_TABLE_ALIAS, ddl);
        Column[] columns = trigger.orderColumnsForTable(metaData);
        ColumnString columnString = buildColumnString(ORIG_TABLE_ALIAS, getNewTriggerValue(), getNewColumnPrefix(), columns, false, supportsBigLobs, concatColumns);
        ddl = StringUtils.replaceTokens("columns", columnString.toString(), ddl);
        ddl = replaceDefaultSchemaAndCatalog(metaData, ddl);
        ddl = StringUtils.replaceTokens("virtualOldNewTable", buildVirtualTableSql(getOldColumnPrefix(), getNewColumnPrefix(), metaData.getColumns()), ddl);
        ddl = StringUtils.replaceTokens("oldColumns", buildColumnString(ORIG_TABLE_ALIAS, getOldTriggerValue(), getOldColumnPrefix(), columns, true, supportsBigLobs, concatColumns).toString(), ddl);
        ddl = eval(columnString.isBlobClob, "containsBlobClobColumns", ddl);
        ddl = StringUtils.replaceTokens("tableName", history == null ? trigger.getSourceTableName() : history.getSourceTableName(), ddl);
        ddl = StringUtils.replaceTokens("schemaName", (history == null ? (resolveSchemaAndCatalogs && trigger.getSourceSchemaName() != null ? trigger.getSourceSchemaName() + "." : "") : (resolveSchemaAndCatalogs && history.getSourceSchemaName() != null ? history.getSourceSchemaName() + "." : "")), ddl);
        columns = metaData.getPrimaryKeyColumnsArray();
        ddl = StringUtils.replaceTokens("oldKeys", buildColumnString(ORIG_TABLE_ALIAS, getOldTriggerValue(), getOldColumnPrefix(), columns, true, supportsBigLobs, concatColumns).toString(), ddl);
        ddl = StringUtils.replaceTokens("oldNewPrimaryKeyJoin", aliasedPrimaryKeyJoin(getOldTriggerValue(), getNewTriggerValue(), columns), ddl);
        ddl = StringUtils.replaceTokens("tableNewPrimaryKeyJoin", aliasedPrimaryKeyJoin(ORIG_TABLE_ALIAS, getNewTriggerValue(), columns), ddl);
        ddl = StringUtils.replaceTokens("primaryKeyWhereString", getPrimaryKeyWhereString(dml == DataEventType.DELETE ? getOldTriggerValue() : getNewTriggerValue(), columns), ddl);
        ddl = StringUtils.replaceTokens("declareOldKeyVariables", buildKeyVariablesDeclare(columns, "old"), ddl);
        ddl = StringUtils.replaceTokens("declareNewKeyVariables", buildKeyVariablesDeclare(columns, "new"), ddl);
        ddl = StringUtils.replaceTokens("oldKeyNames", buildColumnNameString(getOldTriggerValue(), columns), ddl);
        ddl = StringUtils.replaceTokens("newKeyNames", buildColumnNameString(getNewTriggerValue(), columns), ddl);
        ddl = StringUtils.replaceTokens("oldKeyVariables", buildKeyVariablesString(columns, "old"), ddl);
        ddl = StringUtils.replaceTokens("newKeyVariables", buildKeyVariablesString(columns, "new"), ddl);
        ddl = StringUtils.replaceTokens("varNewPrimaryKeyJoin", aliasedPrimaryKeyJoinVar(getNewTriggerValue(), "new", columns), ddl);
        ddl = StringUtils.replaceTokens("varOldPrimaryKeyJoin", aliasedPrimaryKeyJoinVar(getOldTriggerValue(), "old", columns), ddl);
        ddl = StringUtils.replaceTokens("newTriggerValue", getNewTriggerValue(), ddl);
        ddl = StringUtils.replaceTokens("oldTriggerValue", getOldTriggerValue(), ddl);
        ddl = StringUtils.replaceTokens("newColumnPrefix", getNewColumnPrefix(), ddl);
        ddl = StringUtils.replaceTokens("oldColumnPrefix", getOldColumnPrefix(), ddl);
        switch(dml) {
            case DELETE:
                ddl = StringUtils.replaceTokens("curTriggerValue", getOldTriggerValue(), ddl);
                ddl = StringUtils.replaceTokens("curColumnPrefix", getOldColumnPrefix(), ddl);
                break;
            case INSERT:
            case UPDATE:
            default:
                ddl = StringUtils.replaceTokens("curTriggerValue", getNewTriggerValue(), ddl);
                ddl = StringUtils.replaceTokens("curColumnPrefix", getNewColumnPrefix(), ddl);
                break;
        }
        return ddl;
    }

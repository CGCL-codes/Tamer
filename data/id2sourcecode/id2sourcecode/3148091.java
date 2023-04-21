    public String replaceTemplateVariables(DataEventType dml, Trigger trigger, TriggerHistory history, Channel channel, String tablePrefix, Table table, String defaultCatalog, String defaultSchema, String ddl) {
        ddl = FormatUtils.replace("targetTableName", getDefaultTargetTableName(trigger, history), ddl);
        ddl = FormatUtils.replace("triggerName", history.getTriggerNameForDmlType(dml), ddl);
        ddl = FormatUtils.replace("channelName", trigger.getChannelId(), ddl);
        ddl = FormatUtils.replace("triggerHistoryId", Integer.toString(history == null ? -1 : history.getTriggerHistoryId()), ddl);
        String triggerExpression = symmetricDialect.getTransactionTriggerExpression(defaultCatalog, defaultSchema, trigger);
        if (symmetricDialect.isTransactionIdOverrideSupported() && !StringUtils.isBlank(trigger.getTxIdExpression())) {
            triggerExpression = trigger.getTxIdExpression();
        }
        ddl = FormatUtils.replace("txIdExpression", symmetricDialect.preProcessTriggerSqlClause(triggerExpression), ddl);
        ddl = FormatUtils.replace("externalSelect", (trigger.getExternalSelect() == null ? "null" : "(" + symmetricDialect.preProcessTriggerSqlClause(trigger.getExternalSelect()) + ")"), ddl);
        ddl = FormatUtils.replace("syncOnInsertCondition", symmetricDialect.preProcessTriggerSqlClause(trigger.getSyncOnInsertCondition()), ddl);
        ddl = FormatUtils.replace("syncOnUpdateCondition", symmetricDialect.preProcessTriggerSqlClause(trigger.getSyncOnUpdateCondition()), ddl);
        ddl = FormatUtils.replace("syncOnDeleteCondition", symmetricDialect.preProcessTriggerSqlClause(trigger.getSyncOnDeleteCondition()), ddl);
        ddl = FormatUtils.replace("dataHasChangedCondition", symmetricDialect.preProcessTriggerSqlClause(symmetricDialect.getDataHasChangedCondition(trigger)), ddl);
        ddl = FormatUtils.replace("sourceNodeExpression", symmetricDialect.getSourceNodeExpression(), ddl);
        ddl = FormatUtils.replace("oracleLobType", trigger.isUseCaptureLobs() ? "clob" : "long", ddl);
        String syncTriggersExpression = symmetricDialect.getSyncTriggersExpression();
        ddl = FormatUtils.replace("syncOnIncomingBatchCondition", trigger.isSyncOnIncomingBatch() ? Constants.ALWAYS_TRUE_CONDITION : syncTriggersExpression, ddl);
        ddl = FormatUtils.replace("origTableAlias", ORIG_TABLE_ALIAS, ddl);
        Column[] columns = trigger.orderColumnsForTable(table);
        ColumnString columnString = buildColumnString(ORIG_TABLE_ALIAS, newTriggerValue, newColumnPrefix, columns, dml, false, channel, trigger);
        ddl = FormatUtils.replace("columns", columnString.toString(), ddl);
        ddl = replaceDefaultSchemaAndCatalog(ddl);
        ddl = FormatUtils.replace("virtualOldNewTable", buildVirtualTableSql(oldColumnPrefix, newColumnPrefix, table.getColumns()), ddl);
        ddl = FormatUtils.replace("oldColumns", buildColumnString(ORIG_TABLE_ALIAS, oldTriggerValue, oldColumnPrefix, columns, dml, true, channel, trigger).toString(), ddl);
        ddl = eval(columnString.isBlobClob, "containsBlobClobColumns", ddl);
        ddl = FormatUtils.replace("tableName", quote(table.getName()), ddl);
        ddl = FormatUtils.replace("schemaName", history == null ? getSourceTablePrefix(trigger) : getSourceTablePrefix(history), ddl);
        columns = table.getPrimaryKeyColumns();
        ddl = FormatUtils.replace("oldKeys", buildColumnString(ORIG_TABLE_ALIAS, oldTriggerValue, oldColumnPrefix, columns, dml, true, channel, trigger).toString(), ddl);
        ddl = FormatUtils.replace("oldNewPrimaryKeyJoin", aliasedPrimaryKeyJoin(oldTriggerValue, newTriggerValue, columns.length == 0 ? table.getColumns() : columns), ddl);
        ddl = FormatUtils.replace("tableNewPrimaryKeyJoin", aliasedPrimaryKeyJoin(ORIG_TABLE_ALIAS, newTriggerValue, columns.length == 0 ? table.getColumns() : columns), ddl);
        ddl = FormatUtils.replace("primaryKeyWhereString", getPrimaryKeyWhereString(dml == DataEventType.DELETE ? oldTriggerValue : newTriggerValue, table.hasPrimaryKey() ? table.getPrimaryKeyColumns() : table.getColumns()), ddl);
        String builtString = buildColumnNameString(oldTriggerValue, columns);
        ddl = FormatUtils.replace("oldKeyNames", StringUtils.isNotBlank(builtString) ? "," + builtString : "", ddl);
        builtString = buildColumnNameString(newTriggerValue, columns);
        ddl = FormatUtils.replace("newKeyNames", StringUtils.isNotBlank(builtString) ? "," + builtString : "", ddl);
        builtString = buildKeyVariablesString(columns, "old");
        ddl = FormatUtils.replace("oldKeyVariables", StringUtils.isNotBlank(builtString) ? "," + builtString : "", ddl);
        builtString = buildKeyVariablesString(columns, "new");
        ddl = FormatUtils.replace("newKeyVariables", StringUtils.isNotBlank(builtString) ? "," + builtString : "", ddl);
        ddl = FormatUtils.replace("varNewPrimaryKeyJoin", aliasedPrimaryKeyJoinVar(newTriggerValue, "new", columns), ddl);
        ddl = FormatUtils.replace("varOldPrimaryKeyJoin", aliasedPrimaryKeyJoinVar(oldTriggerValue, "old", columns), ddl);
        ddl = FormatUtils.replace("newTriggerValue", newTriggerValue, ddl);
        ddl = FormatUtils.replace("oldTriggerValue", oldTriggerValue, ddl);
        ddl = FormatUtils.replace("newColumnPrefix", newColumnPrefix, ddl);
        ddl = FormatUtils.replace("oldColumnPrefix", oldColumnPrefix, ddl);
        ddl = FormatUtils.replace("prefixName", tablePrefix, ddl);
        ddl = replaceDefaultSchemaAndCatalog(ddl);
        ddl = FormatUtils.replace("oracleToClob", trigger.isUseCaptureLobs() ? "to_clob('')||" : "", ddl);
        switch(dml) {
            case DELETE:
                ddl = FormatUtils.replace("curTriggerValue", oldTriggerValue, ddl);
                ddl = FormatUtils.replace("curColumnPrefix", oldColumnPrefix, ddl);
                break;
            case INSERT:
            case UPDATE:
            default:
                ddl = FormatUtils.replace("curTriggerValue", newTriggerValue, ddl);
                ddl = FormatUtils.replace("curColumnPrefix", newColumnPrefix, ddl);
                break;
        }
        return ddl;
    }

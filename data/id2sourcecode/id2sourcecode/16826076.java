    public Data createData(Trigger trigger, String whereClause) {
        Data data = null;
        if (trigger != null) {
            TriggerHistory triggerHistory = triggerRouterService.getNewestTriggerHistoryForTrigger(trigger.getTriggerId(), trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), trigger.getSourceTableName());
            if (triggerHistory == null) {
                triggerHistory = triggerRouterService.findTriggerHistory(trigger.getSourceTableName());
                if (triggerHistory == null) {
                    triggerHistory = triggerRouterService.findTriggerHistory(trigger.getSourceTableName().toUpperCase());
                }
            }
            if (triggerHistory != null) {
                String rowData = null;
                String pkData = null;
                if (whereClause != null) {
                    rowData = (String) sqlTemplate.queryForObject(symmetricDialect.createCsvDataSql(trigger, triggerHistory, configurationService.getChannel(trigger.getChannelId()), whereClause), String.class);
                    if (rowData != null) {
                        rowData = rowData.trim();
                    }
                    pkData = (String) sqlTemplate.queryForObject(symmetricDialect.createCsvPrimaryKeySql(trigger, triggerHistory, configurationService.getChannel(trigger.getChannelId()), whereClause), String.class);
                    if (pkData != null) {
                        pkData = pkData.trim();
                    }
                }
                data = new Data(trigger.getSourceTableName(), DataEventType.UPDATE, rowData, pkData, triggerHistory, trigger.getChannelId(), null, null);
            }
        }
        return data;
    }

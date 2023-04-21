    public void insertSqlEvent(final Node targetNode, final Trigger trigger, String sql, boolean isLoad) {
        TriggerHistory history = triggerRouterService.getNewestTriggerHistoryForTrigger(trigger.getTriggerId(), trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), trigger.getSourceTableName());
        Data data = new Data(history.getSourceTableName(), DataEventType.SQL, CsvUtils.escapeCsvData(sql), null, history, trigger.getChannelId(), null, null);
        insertDataAndDataEventAndOutgoingBatch(data, targetNode.getNodeId(), Constants.UNKNOWN_ROUTER_ID, isLoad);
    }

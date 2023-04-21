    public void insertPurgeEvent(final Node targetNode, final TriggerRouter triggerRouter, boolean isLoad) {
        String sql = symmetricDialect.createPurgeSqlFor(targetNode, triggerRouter);
        Trigger trigger = triggerRouter.getTrigger();
        TriggerHistory history = triggerRouterService.getNewestTriggerHistoryForTrigger(trigger.getTriggerId(), trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), trigger.getSourceTableName());
        Data data = new Data(history.getSourceTableName(), DataEventType.SQL, CsvUtils.escapeCsvData(sql), null, history, triggerRouter.getTrigger().getChannelId(), null, null);
        insertDataAndDataEventAndOutgoingBatch(data, targetNode.getNodeId(), triggerRouter.getRouter().getRouterId(), isLoad);
    }

    public void insertReloadEvent(final Node targetNode, final TriggerRouter triggerRouter, final String overrideInitialLoadSelect) {
        TriggerHistory history = lookupTriggerHistory(triggerRouter.getTrigger());
        Data data = new Data(history.getSourceTableName(), DataEventType.RELOAD, overrideInitialLoadSelect != null ? overrideInitialLoadSelect : triggerRouter.getInitialLoadSelect(), null, history, triggerRouter.getTrigger().getChannelId(), null, null);
        insertDataAndDataEventAndOutgoingBatch(data, targetNode.getNodeId(), triggerRouter.getRouter().getRouterId(), true);
    }

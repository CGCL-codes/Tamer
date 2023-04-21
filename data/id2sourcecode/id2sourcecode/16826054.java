    public void insertCreateEvent(final Node targetNode, final TriggerRouter triggerRouter, String xml, boolean isLoad) {
        Trigger trigger = triggerRouter.getTrigger();
        TriggerHistory history = triggerRouterService.getNewestTriggerHistoryForTrigger(trigger.getTriggerId(), trigger.getSourceCatalogName(), trigger.getSourceSchemaName(), trigger.getSourceTableName());
        Data data = new Data(triggerRouter.getTrigger().getSourceTableName(), DataEventType.CREATE, CsvUtils.escapeCsvData(xml), null, history, parameterService.is(ParameterConstants.INITIAL_LOAD_USE_RELOAD_CHANNEL) && isLoad ? Constants.CHANNEL_RELOAD : triggerRouter.getTrigger().getChannelId(), null, null);
        try {
            insertDataAndDataEventAndOutgoingBatch(data, targetNode.getNodeId(), Constants.UNKNOWN_ROUTER_ID, isLoad);
        } catch (UniqueKeyException e) {
            if (e.getRootCause() != null && e.getRootCause() instanceof DataTruncation) {
                log.error("Table data definition XML was too large and failed.  The feature to send table creates during the initial load may be limited on your platform.  You may need to set the initial.load.create.first parameter to false.");
            }
            throw e;
        }
    }

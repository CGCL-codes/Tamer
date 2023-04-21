    public void extractConfigurationStandalone(Node node, Writer writer, String... tablesToExclude) {
        Batch batch = new Batch(BatchInfo.VIRTUAL_BATCH_FOR_REGISTRATION, Constants.CHANNEL_CONFIG, symmetricDialect.getBinaryEncoding(), node.getNodeId());
        NodeGroupLink nodeGroupLink = new NodeGroupLink(parameterService.getNodeGroupId(), node.getNodeGroupId());
        List<TriggerRouter> triggerRouters = triggerRouterService.buildTriggerRoutersForSymmetricTables(StringUtils.isBlank(node.getSymmetricVersion()) ? Version.version() : node.getSymmetricVersion(), nodeGroupLink, tablesToExclude);
        List<SelectFromTableEvent> initialLoadEvents = new ArrayList<SelectFromTableEvent>(triggerRouters.size() * 2);
        for (int i = triggerRouters.size() - 1; i >= 0; i--) {
            TriggerRouter triggerRouter = triggerRouters.get(i);
            TriggerHistory triggerHistory = triggerRouterService.getNewestTriggerHistoryForTrigger(triggerRouter.getTrigger().getTriggerId(), null, null, triggerRouter.getTrigger().getSourceTableName());
            if (triggerHistory == null) {
                Table table = symmetricDialect.getTable(triggerRouter.getTrigger(), false);
                if (table == null) {
                    throw new IllegalStateException("Could not find a required table: " + triggerRouter.getTrigger().getSourceTableName());
                }
                triggerHistory = new TriggerHistory(table, triggerRouter.getTrigger());
                triggerHistory.setTriggerHistoryId(Integer.MAX_VALUE - i);
            }
            StringBuilder sql = new StringBuilder(symmetricDialect.createPurgeSqlFor(node, triggerRouter));
            addPurgeCriteriaToConfigurationTables(triggerRouter.getTrigger().getSourceTableName(), sql);
            String sourceTable = triggerHistory.getSourceTableName();
            Data data = new Data(1, null, sql.toString(), DataEventType.SQL, sourceTable, null, triggerHistory, triggerRouter.getTrigger().getChannelId(), null, null);
            data.putAttribute(Data.ATTRIBUTE_ROUTER_ID, triggerRouter.getRouter().getRouterId());
            initialLoadEvents.add(new SelectFromTableEvent(data));
        }
        for (int i = 0; i < triggerRouters.size(); i++) {
            TriggerRouter triggerRouter = triggerRouters.get(i);
            TriggerHistory triggerHistory = triggerRouterService.getNewestTriggerHistoryForTrigger(triggerRouter.getTrigger().getTriggerId(), null, null, null);
            if (triggerHistory == null) {
                triggerHistory = new TriggerHistory(symmetricDialect.getTable(triggerRouter.getTrigger(), false), triggerRouter.getTrigger());
                triggerHistory.setTriggerHistoryId(Integer.MAX_VALUE - i);
            }
            if (!triggerRouter.getTrigger().getSourceTableName().endsWith(TableConstants.SYM_NODE_IDENTITY)) {
                initialLoadEvents.add(new SelectFromTableEvent(node, triggerRouter, triggerHistory));
            } else {
                Data data = new Data(1, null, node.getNodeId(), DataEventType.INSERT, triggerHistory.getSourceTableName(), null, triggerHistory, triggerRouter.getTrigger().getChannelId(), null, null);
                initialLoadEvents.add(new SelectFromTableEvent(data));
            }
        }
        SelectFromTableSource source = new SelectFromTableSource(batch, initialLoadEvents);
        ExtractDataReader dataReader = new ExtractDataReader(this.symmetricDialect.getPlatform(), source);
        ProtocolDataWriter dataWriter = new ProtocolDataWriter(nodeService.findIdentityNodeId(), writer);
        DataProcessor processor = new DataProcessor(dataReader, dataWriter);
        processor.process();
        if (triggerRouters.size() == 0) {
            log.error("{} attempted registration, but was sent an empty configuration", node);
        }
    }

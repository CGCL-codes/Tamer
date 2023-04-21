    protected void testUseOfDefaultChannel(boolean deleteChannel) throws Exception {
        Channel unrecognizedChannel = new Channel("gobble-t-gook", 1);
        resetBatches();
        if (deleteChannel) {
            getConfigurationService().saveChannel(unrecognizedChannel, true);
        }
        NodeChannel otherChannel = getConfigurationService().getNodeChannel(TestConstants.TEST_CHANNEL_ID_OTHER, false);
        otherChannel.setBatchAlgorithm("nontransactional");
        otherChannel.setMaxBatchSize(100);
        getConfigurationService().saveChannel(otherChannel, true);
        TriggerRouter trigger1 = getTestRoutingTableTrigger(TEST_TABLE_1);
        trigger1.getRouter().setRouterType("default");
        trigger1.getTrigger().setChannelId(unrecognizedChannel.getChannelId());
        getTriggerRouterService().saveTriggerRouter(trigger1);
        TriggerRouter trigger2 = getTestRoutingTableTrigger(TEST_TABLE_2);
        trigger2.getRouter().setRouterType("default");
        getTriggerRouterService().saveTriggerRouter(trigger2);
        getTriggerRouterService().syncTriggers();
        if (deleteChannel) {
            getConfigurationService().deleteChannel(unrecognizedChannel);
        }
        try {
            getRouterService().routeData();
            resetBatches();
            insert(TEST_TABLE_1, 10, true, null);
            getRouterService().routeData();
            insert(TEST_TABLE_2, 10, true, null);
            getRouterService().routeData();
            insert(TEST_TABLE_2, 10, true, null);
            getRouterService().routeData();
            getRouterService().routeData();
            OutgoingBatches batches = getOutgoingBatchService().getOutgoingBatches(NODE_GROUP_NODE_1, false);
            Assert.assertEquals(3, batches.getBatches().size());
            Assert.assertEquals(Constants.CHANNEL_DEFAULT, batches.getBatches().get(2).getChannelId());
        } finally {
            trigger1.getTrigger().setChannelId(TestConstants.TEST_CHANNEL_ID);
            getTriggerRouterService().saveTriggerRouter(trigger1);
            getTriggerRouterService().syncTriggers();
            resetBatches();
        }
    }

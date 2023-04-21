    public OutgoingBatches getOutgoingBatches(Node node, boolean includeDisabledChannels) {
        long ts = System.currentTimeMillis();
        final int maxNumberOfBatchesToSelect = parameterService.getInt(ParameterConstants.OUTGOING_BATCH_MAX_BATCHES_TO_SELECT, 1000);
        List<OutgoingBatch> list = (List<OutgoingBatch>) sqlTemplate.query(getSql("selectOutgoingBatchPrefixSql", "selectOutgoingBatchSql"), maxNumberOfBatchesToSelect, new OutgoingBatchMapper(includeDisabledChannels, true), new Object[] { node.getNodeId(), OutgoingBatch.Status.NE.name(), OutgoingBatch.Status.QY.name(), OutgoingBatch.Status.SE.name(), OutgoingBatch.Status.LD.name(), OutgoingBatch.Status.ER.name() }, null);
        OutgoingBatches batches = new OutgoingBatches(list);
        List<NodeChannel> channels = configurationService.getNodeChannels(node.getNodeId(), true);
        batches.sortChannels(channels);
        List<OutgoingBatch> keepers = new ArrayList<OutgoingBatch>();
        for (NodeChannel channel : channels) {
            if (parameterService.is(ParameterConstants.DATA_EXTRACTOR_ENABLED) || channel.getChannelId().equals(Constants.CHANNEL_CONFIG)) {
                keepers.addAll(batches.getBatchesForChannelWindows(node, channel, configurationService.getNodeGroupChannelWindows(parameterService.getNodeGroupId(), channel.getChannelId())));
            }
        }
        batches.setBatches(keepers);
        long executeTimeInMs = System.currentTimeMillis() - ts;
        if (executeTimeInMs > Constants.LONG_OPERATION_THRESHOLD) {
            log.warn("{} took {} ms", "selecting batches to extract", executeTimeInMs);
        }
        return batches;
    }

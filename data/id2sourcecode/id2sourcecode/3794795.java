    protected void filterForChannels(OutgoingBatches batches, NodeChannel... channels) {
        for (Iterator<OutgoingBatch> iterator = batches.getBatches().iterator(); iterator.hasNext(); ) {
            OutgoingBatch outgoingBatch = iterator.next();
            boolean foundChannel = false;
            for (NodeChannel nodeChannel : channels) {
                if (outgoingBatch.getChannelId().equals(nodeChannel.getChannelId())) {
                    foundChannel = true;
                }
            }
            if (!foundChannel) {
                iterator.remove();
            }
        }
    }

    protected int countBatchesForChannel(OutgoingBatches batches, NodeChannel channel) {
        int count = 0;
        for (Iterator<OutgoingBatch> iterator = batches.getBatches().iterator(); iterator.hasNext(); ) {
            OutgoingBatch outgoingBatch = iterator.next();
            count += outgoingBatch.getChannelId().equals(channel.getChannelId()) ? 1 : 0;
        }
        return count;
    }

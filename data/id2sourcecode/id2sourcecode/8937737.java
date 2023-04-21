    public List<OutgoingBatch> filterBatchesForInactiveChannels() {
        List<OutgoingBatch> filtered = new ArrayList<OutgoingBatch>();
        for (OutgoingBatch batch : batches) {
            if (!activeChannelIds.contains(batch.getChannelId())) {
                filtered.add(batch);
            }
        }
        batches.removeAll(filtered);
        return filtered;
    }

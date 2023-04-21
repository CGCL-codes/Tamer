    public List<OutgoingBatch> getBatchesForChannels(Set<String> channelIds) {
        List<OutgoingBatch> batchList = new ArrayList<OutgoingBatch>();
        if (channelIds != null) {
            for (OutgoingBatch batch : batches) {
                if (channelIds.contains(batch.getChannelId())) {
                    batchList.add(batch);
                }
            }
        }
        return batchList;
    }

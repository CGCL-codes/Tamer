    public List<OutgoingBatch> getBatchesForChannel(String channelId) {
        List<OutgoingBatch> batchList = new ArrayList<OutgoingBatch>();
        if (channelId != null) {
            for (OutgoingBatch batch : batches) {
                if (channelId.equals(batch.getChannelId())) {
                    batchList.add(batch);
                }
            }
        }
        return batchList;
    }

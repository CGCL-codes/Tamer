    public List<OutgoingBatch> getBatchesForChannel(Channel channel) {
        List<OutgoingBatch> batchList = new ArrayList<OutgoingBatch>();
        if (channel != null) {
            batchList = getBatchesForChannel(channel.getChannelId());
        }
        return batchList;
    }

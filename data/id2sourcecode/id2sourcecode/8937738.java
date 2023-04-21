    public void sortChannels(List<NodeChannel> channels) {
        final HashMap<String, Date> errorChannels = new HashMap<String, Date>();
        for (OutgoingBatch batch : batches) {
            if (batch.isErrorFlag()) {
                errorChannels.put(batch.getChannelId(), batch.getLastUpdatedTime());
            }
        }
        Collections.sort(channels, new Comparator<NodeChannel>() {

            public int compare(NodeChannel b1, NodeChannel b2) {
                boolean isError1 = errorChannels.containsKey(b1.getChannelId());
                boolean isError2 = errorChannels.containsKey(b2.getChannelId());
                if (!isError1 && !isError2) {
                    return b1.getProcessingOrder() < b2.getProcessingOrder() ? -1 : 1;
                } else if (isError1 && isError2) {
                    return errorChannels.get(b1.getChannelId()).compareTo(errorChannels.get(b2.getChannelId()));
                } else if (!isError1 && isError2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        for (NodeChannel nodeChannel : channels) {
            long extractPeriodMillis = nodeChannel.getExtractPeriodMillis();
            Date lastExtractedTime = nodeChannel.getLastExtractTime();
            if ((extractPeriodMillis < 1) || (lastExtractedTime == null) || (Calendar.getInstance().getTimeInMillis() - lastExtractedTime.getTime() >= extractPeriodMillis)) {
                addActiveChannel(nodeChannel);
            }
        }
        filterBatchesForInactiveChannels();
    }

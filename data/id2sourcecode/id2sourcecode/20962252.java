    public void incrementDataBytesExtracted(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataBytesExtracted(count);
        } finally {
            channelStatsLock.release();
        }
    }

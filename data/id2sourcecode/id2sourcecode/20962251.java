    public void incrementDataExtracted(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataExtracted(count);
        } finally {
            channelStatsLock.release();
        }
    }

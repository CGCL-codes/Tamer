    public void incrementDataLoaded(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataLoaded(count);
        } finally {
            channelStatsLock.release();
        }
    }

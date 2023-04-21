    public void incrementDataSent(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataSent(count);
        } finally {
            channelStatsLock.release();
        }
    }

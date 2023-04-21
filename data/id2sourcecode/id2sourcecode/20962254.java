    public void incrementDataEventInserted(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataEventInserted(count);
        } finally {
            channelStatsLock.release();
        }
    }

    public void incrementDataRouted(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataRouted(count);
        } finally {
            channelStatsLock.release();
        }
    }

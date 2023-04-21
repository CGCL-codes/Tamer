    public void incrementDataLoadedErrors(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataLoadedErrors(count);
        } finally {
            channelStatsLock.release();
        }
    }

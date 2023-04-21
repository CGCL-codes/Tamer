    public void incrementDataBytesLoaded(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataBytesLoaded(count);
        } finally {
            channelStatsLock.release();
        }
    }

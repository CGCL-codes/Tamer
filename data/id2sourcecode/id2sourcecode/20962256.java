    public void incrementDataBytesSent(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataBytesSent(count);
        } finally {
            channelStatsLock.release();
        }
    }

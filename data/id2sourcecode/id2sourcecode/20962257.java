    public void incrementDataSentErrors(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataSentErrors(count);
        } finally {
            channelStatsLock.release();
        }
    }

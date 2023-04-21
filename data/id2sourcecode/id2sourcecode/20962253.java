    public void incrementDataExtractedErrors(String channelId, long count) {
        channelStatsLock.acquireUninterruptibly();
        try {
            getChannelStats(channelId).incrementDataExtractedErrors(count);
        } finally {
            channelStatsLock.release();
        }
    }

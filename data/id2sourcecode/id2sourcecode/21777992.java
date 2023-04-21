    private void internalStartRequest() {
        synchronized (this) {
            stat = STAT_REQUEST_HEADER;
            logger.debug("startRequest requestHeaderBuffer length:" + BuffersUtil.remaining(requestHeaderBuffer) + ":" + getPoolId() + ":cid:" + getChannelId());
            requestHeaderLength = BuffersUtil.remaining(requestHeaderBuffer);
            asyncWrite(CONTEXT_HEADER, PoolManager.duplicateBuffers(requestHeaderBuffer));
        }
    }

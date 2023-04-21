    private void internalStartRequest() {
        synchronized (this) {
            stat = STAT_REQUEST_HEADER;
            logger.debug("startRequest requestHeaderBuffer length:" + BuffersUtil.remaining(requestHeaderBuffer) + ":" + getPoolId() + ":cid:" + getChannelId());
            requestHeaderLength = BuffersUtil.remaining(requestHeaderBuffer);
            if (scheduler != null) {
                scheduler.scheduleWrite(CONTEXT_HEADER, requestHeaderBuffer);
            } else {
                headerActualWriteTime = System.currentTimeMillis();
                asyncWrite(CONTEXT_HEADER, requestHeaderBuffer);
            }
            requestHeaderBuffer = null;
            if (requestBodyBuffer != null) {
                stat = STAT_REQUEST_BODY;
                long length = BuffersUtil.remaining(requestBodyBuffer);
                requestContentWriteLength += length;
                if (scheduler != null) {
                    scheduler.scheduleWrite(CONTEXT_BODY, requestBodyBuffer);
                } else {
                    bodyActualWriteTime = System.currentTimeMillis();
                    asyncWrite(CONTEXT_BODY, requestBodyBuffer);
                }
                requestBodyBuffer = null;
            }
        }
        if (requestContentWriteLength >= requestContentLength) {
        }
    }

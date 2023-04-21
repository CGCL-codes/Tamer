    public void responseEnd() {
        synchronized (this) {
            if (isResponseEnd || getChannelId() == -1) {
                return;
            }
            logger.debug("responseEnd called.handler:" + toString());
            isResponseEnd = true;
            if (isFlushFirstResponse == false) {
                flushFirstResponse(null);
                isFlushFirstResponse = true;
            }
            endOfResponse();
        }
    }

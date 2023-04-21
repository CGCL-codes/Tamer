    public void onTimeout(Object userContext) {
        logger.debug("#timeout.cid:" + getChannelId());
        asyncClose(userContext);
        if (isKeepAlive == false) {
            onRequestFailure(stat, FAILURE_TIMEOUT);
        }
        isKeepAlive = false;
        super.onTimeout(userContext);
    }

    public void onFailure(Object userContext, Throwable t) {
        logger.debug("#failure.cid:" + getChannelId(), t);
        isKeepAlive = false;
        asyncClose(userContext);
        onRequestFailure(stat, t);
        super.onFailure(userContext, t);
    }

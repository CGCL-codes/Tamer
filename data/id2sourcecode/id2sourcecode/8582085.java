    public void onClosed(Object userContext) {
        logger.debug("#closed.cid:" + getChannelId());
        isKeepAlive = false;
        onRequestEnd(STAT_END);
        super.onClosed(userContext);
    }

    public void onFinished() {
        logger.debug("#finished.cid:" + getChannelId());
        isKeepAlive = false;
        onRequestEnd(STAT_END);
        super.onFinished();
    }

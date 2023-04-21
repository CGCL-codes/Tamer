    public void onFinished() {
        logger.debug("#finished.cid:" + getChannelId());
        responseEnd();
        super.onFinished();
    }

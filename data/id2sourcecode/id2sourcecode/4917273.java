    @Override
    public void onWcFailure(Object userContext, int stat, Throwable t) {
        logger.debug("#wcFailure cid:" + getChannelId());
        closeWebSocket("500");
        unref();
    }

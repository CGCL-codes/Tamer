    public void onReadTimeout(Object userContext) {
        logger.debug("#readTimeout.cid:" + getChannelId());
        wsProtocol.onReadTimeout();
    }

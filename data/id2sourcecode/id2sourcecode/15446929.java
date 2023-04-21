    public void onFailure(Object userContext, Throwable t) {
        logger.debug("#failure.cid:" + getChannelId(), t);
        asyncClose(userContext);
        responseEnd();
    }

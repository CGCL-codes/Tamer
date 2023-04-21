    public void onFailure(Object userContext, Throwable t) {
        logger.debug("#failer.cid:" + getChannelId() + ":" + t.getMessage());
        asyncClose(userContext);
        super.onFailure(userContext, t);
    }

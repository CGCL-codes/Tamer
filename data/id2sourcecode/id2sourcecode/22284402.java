    public void onFailure(Object userContext, Throwable t) {
        logger.debug("#failer.cid:" + getChannelId() + ":" + t.getMessage());
        super.onFailure(userContext, t);
    }

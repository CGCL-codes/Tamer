    public void onTimeout(Object userContext) {
        logger.debug("#timeout.cid:" + getChannelId());
        super.onTimeout(userContext);
    }

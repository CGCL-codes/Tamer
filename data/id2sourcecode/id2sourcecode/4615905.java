    public void onAccepted(Object userContext) {
        setReadTimeout(getConfig().getAcceptTimeout());
        logger.debug("#accepted.cid:" + getChannelId());
        isFirstRead = true;
        getKeepAliveContext(true);
        startTime = new Date();
        onStartRequest();
    }

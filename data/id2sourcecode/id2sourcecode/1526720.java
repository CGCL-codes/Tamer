    public void onFinished() {
        logger.debug("#onFinished cid:" + getChannelId());
        responseEnd();
        KeepAliveContext keepAliveContext = getKeepAliveContext();
        if (keepAliveContext != null) {
            keepAliveContext.finishedOfServerHandler();
        }
        super.onFinished();
    }

    public void waitForNextRequest() {
        logger.debug("#waitForNextRequest cid:" + getChannelId());
        DispatchHandler handler = (DispatchHandler) super.forwardHandler(DispatchHandler.class);
        if (handler == null) {
            logger.warn("fail to forward Dispatcher.Can't keepAlive.");
            return;
        }
        handler.onStartRequest();
    }

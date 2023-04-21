    public void onClosed(Object userContext) {
        logger.debug("#closed client.cid:" + client.getChannelId());
        if (isConnected) {
            server.asyncClose(userContext);
        }
        super.onClosed(userContext);
    }

    private void onWcConnected() {
        logger.debug("#wsConnected cid:" + getChannelId());
        if (wsClient != null) {
            wsClient.onWcConnected(userContext);
        }
    }

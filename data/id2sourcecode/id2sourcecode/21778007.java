    private void onWcProxyConnected() {
        logger.debug("#wsProxyConnected cid:" + getChannelId());
        if (wsClient != null) {
            wsClient.onWcProxyConnected(userContext);
        }
    }

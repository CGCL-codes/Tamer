    private void onWebProxyConnected() {
        logger.debug("#webProxyConnected cid:" + getChannelId());
        if (webClient != null) {
            webClient.onWebProxyConnected(userContext);
        }
    }

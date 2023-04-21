    private void onWcMessage(CacheBuffer message) {
        logger.debug("#message binary cid:" + getChannelId());
        if (wsClient != null) {
            wsClient.onWcMessage(userContext, message);
        }
    }

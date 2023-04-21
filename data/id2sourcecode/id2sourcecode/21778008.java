    private void onWcHandshaked(String subprotocol) {
        logger.debug("#wsHandshaked cid:" + getChannelId());
        if (wsClient != null) {
            wsClient.onWcHandshaked(userContext, subprotocol);
        }
    }

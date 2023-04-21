    private void onWebHandshaked() {
        logger.debug("#webHandshaked cid:" + getChannelId());
        if (webClient != null) {
            webClient.onWebHandshaked(userContext);
        }
    }

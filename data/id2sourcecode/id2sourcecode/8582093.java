    private void onWebConnected() {
        logger.debug("#webConnected cid:" + getChannelId());
        if (webClient != null) {
            webClient.onWebConnected(userContext);
        }
    }

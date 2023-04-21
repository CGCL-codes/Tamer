    private void onResponseBody(ByteBuffer[] buffer) {
        logger.debug("#responseBody cid:" + getChannelId());
        if (webClient != null) {
            webClient.onResponseBody(userContext, buffer);
        }
    }

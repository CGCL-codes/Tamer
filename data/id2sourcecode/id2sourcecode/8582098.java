    private void onResponseHeader(HeaderParser responseHeader) {
        logger.debug("#responseHeader cid:" + getChannelId());
        if (webClient != null) {
            webClient.onResponseHeader(userContext, responseHeader);
        }
    }

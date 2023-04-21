    private void onWcResponseHeader(HeaderParser responseHeader) {
        logger.debug("#writtenRequestHeader cid:" + getChannelId());
        if (wsClient != null) {
            wsClient.onWcResponseHeader(userContext, responseHeader);
        }
    }

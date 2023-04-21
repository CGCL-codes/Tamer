    private void onWrittenRequestHeader() {
        logger.debug("#writtenRequestHeader cid:" + getChannelId());
        if (webClient != null) {
            webClient.onWrittenRequestHeader(userContext);
        }
    }

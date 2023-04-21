    private void onWrittenRequestBody() {
        logger.debug("#writtenRequestBody cid:" + getChannelId());
        if (webClient != null) {
            webClient.onWrittenRequestBody(userContext);
        }
    }

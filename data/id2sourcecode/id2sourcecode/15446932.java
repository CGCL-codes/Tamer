    public void onResponseBody(Object userContext, ByteBuffer[] buffer) {
        logger.debug("#responseBody.cid:" + getChannelId());
        if (isTryAgain || isReplace) {
            PoolManager.poolBufferInstance(buffer);
            return;
        }
        if (injector != null) {
            logger.debug("inject add contents cid:" + getChannelId());
            buffer = injector.onResponseBody(buffer);
        }
        responseBody(buffer);
    }

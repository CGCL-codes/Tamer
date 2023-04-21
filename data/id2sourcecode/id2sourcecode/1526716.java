    public void onReadPlain(Object userContext, ByteBuffer[] buffers) {
        logger.debug("#onReadPlain cid:" + getChannelId());
        ChunkContext requestChunkContext = getRequestContext().getRequestChunkContext();
        if (requestChunkContext.isEndOfData()) {
            PoolManager.poolBufferInstance(buffers);
            return;
        }
        if (buffers != null) {
            requestReadBody += BuffersUtil.remaining(buffers);
            requestBody(requestChunkContext.decodeChunk(buffers));
        }
        if (!requestChunkContext.isEndOfData()) {
            asyncRead(null);
            return;
        }
        getAccessLog().setTimeCheckPint(AccessLog.TimePoint.requestBody);
        startResponseReqBody();
        return;
    }

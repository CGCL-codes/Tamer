    @Override
    public void onBuffer(ByteBuffer[] buffers) {
        logger.debug("WsHiXie75#onBuffer cid:" + handler.getChannelId());
        for (ByteBuffer buffer : buffers) {
            parseMessage(buffer);
        }
        handler.asyncRead(null);
    }

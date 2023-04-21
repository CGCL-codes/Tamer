    @Override
    public void onBuffer(ByteBuffer[] buffers) {
        logger.debug("WsHybi10#onBuffer cid:" + handler.getChannelId());
        try {
            for (ByteBuffer buffer : buffers) {
                if (frame.parse(buffer)) {
                    doFrame();
                }
            }
            PoolManager.poolArrayInstance(buffers);
            if (frame.getPayloadLength() > getWebSocketMessageLimit()) {
                logger.debug("WsHybi10#doFrame too long frame.frame.getPayloadLength():" + frame.getPayloadLength());
                sendClose(WsHybiFrame.CLOSE_MESSAGE_TOO_BIG, "too long frame");
            }
            handler.asyncRead(null);
        } catch (RuntimeException e) {
            logger.error("Hybi10 parse error.", e);
            handler.asyncClose(null);
        }
    }

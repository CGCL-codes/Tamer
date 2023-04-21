    @Override
    public void onBuffer(ByteBuffer[] buffers) {
        logger.debug("WsHiXie76#onBuffer cid:" + handler.getChannelId());
        if (handshakeStat == 1) {
            if (wsShakehand(handler.getRequestHeader(), buffers) == false) {
                return;
            }
        }
        for (ByteBuffer buffer : buffers) {
            parseMessage(buffer);
        }
        handler.asyncRead(null);
    }

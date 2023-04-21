    @Override
    public void onReadTimeout() {
        logger.debug("WsHybi10#onReadTimeout cid:" + handler.getChannelId());
        ByteBuffer[] buffers = WsHybiFrame.createPingFrame(isWebSocketResponseMask(), "ping:" + System.currentTimeMillis());
        handler.asyncWrite(null, buffers);
        handler.asyncRead(null);
    }

    @Override
    public void postMessage(ByteBuffer[] msgs) {
        logger.debug("WsHiXie76#postMessage(bin) cid:" + handler.getChannelId());
        PoolManager.poolBufferInstance(msgs);
        throw new UnsupportedOperationException("postMessage binary mode");
    }

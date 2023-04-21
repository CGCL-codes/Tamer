    public void onRead(Object userContext, ByteBuffer[] buffers) {
        logger.debug("#read client.cid:" + client.getChannelId());
        lastIo = System.currentTimeMillis();
        server.asyncWrite(WRITE_REQUEST, buffers);
        asyncRead(READ_REQUEST);
    }

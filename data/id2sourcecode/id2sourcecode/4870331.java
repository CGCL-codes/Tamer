    public void onReadPlain(Object userContext, ByteBuffer[] buffers) {
        logger.debug("#readPlain client.id:" + getChannelId());
        if (!requestDecodeHeader.isParseEnd()) {
            for (int i = 0; i < buffers.length; i++) {
                buffers[i].mark();
                boolean isEnd = requestDecodeHeader.parse(buffers[i]);
                buffers[i].reset();
                if (isEnd) {
                    requestDecodeHeader.getBodyBuffer();
                    break;
                }
            }
        }
        server.asyncWrite(null, buffers);
        client.asyncRead(null);
        lastIo = System.currentTimeMillis();
        return;
    }

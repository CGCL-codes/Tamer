    @Override
    public void postMessage(String message) {
        logger.debug("WsHiXie75#postMessage(txt) cid:" + handler.getChannelId());
        tracePostMessage(message);
        ByteBuffer[] bufs = BuffersUtil.newByteBufferArray(3);
        bufs[0] = ByteBuffer.wrap(START_FRAME);
        try {
            bufs[1] = ByteBuffer.wrap(message.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("fail to getBytes." + message);
            handler.asyncClose(null);
            return;
        }
        bufs[2] = ByteBuffer.wrap(END_FRAME);
        handler.asyncWrite(null, bufs);
    }

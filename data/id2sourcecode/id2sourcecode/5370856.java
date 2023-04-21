    @Override
    public void postMessage(ByteBuffer[] message) {
        logger.debug("WsHybi10#postMessage(bin) cid:" + handler.getChannelId());
        postMessage(new PostRequest(message));
    }

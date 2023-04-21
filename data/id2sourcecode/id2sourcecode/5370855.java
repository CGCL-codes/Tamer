    @Override
    public void postMessage(String message) {
        logger.debug("WsHybi10#postMessage(txt) cid:" + handler.getChannelId());
        postMessage(new PostRequest(message));
    }

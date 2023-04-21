    @Override
    public void postMessage(AsyncBuffer msgs) {
        logger.debug("WsHiXie75#postMessage(bin) cid:" + handler.getChannelId());
        if (msgs instanceof PoolBase) {
            ((PoolBase) msgs).unref();
        }
        throw new UnsupportedOperationException("postMessage binary mode");
    }

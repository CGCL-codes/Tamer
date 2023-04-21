    public boolean onHandshaked() {
        logger.debug("#handshaked.cid:" + getChannelId());
        asyncRead(CONTEXT_HEADER);
        internalStartRequest();
        return false;
    }

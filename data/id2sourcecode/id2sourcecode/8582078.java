    public boolean onHandshaked() {
        logger.debug("#handshaked.cid:" + getChannelId());
        onWebHandshaked();
        asyncRead(CONTEXT_HEADER);
        internalStartRequest();
        return false;
    }

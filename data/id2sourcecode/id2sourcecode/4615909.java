    public boolean onHandshaked() {
        logger.debug("#handshaked.cid:" + getChannelId());
        handshakeTime = System.currentTimeMillis() - startTime.getTime();
        return true;
    }

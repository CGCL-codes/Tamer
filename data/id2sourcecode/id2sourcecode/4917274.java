    @Override
    public void onWcHandshaked(Object userContext, String subprotocol) {
        logger.debug("#wcHandshaked cid:" + getChannelId() + " subprotocol:" + subprotocol);
        doHandshake(subprotocol);
    }

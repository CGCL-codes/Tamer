    @Override
    public void onWsOpen(String subprotocol) {
        logger.debug("#wsOpen cid:" + getChannelId());
        postMessage("OK");
    }

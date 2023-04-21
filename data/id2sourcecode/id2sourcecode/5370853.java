    @Override
    public void onClose(short code, String reason) {
        logger.debug("WsHybi10#onClose cid:" + handler.getChannelId());
        if (handler == null) {
            return;
        }
        logger.debug("WsHybi10#onClose cid:" + handler.getChannelId());
        callOnWsClose(code, reason);
        if (code != WsHybiFrame.CLOSE_UNKOWN) {
            sendClose((short) code, reason);
        }
    }

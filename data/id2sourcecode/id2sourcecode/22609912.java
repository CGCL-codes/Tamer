    @Override
    public void onClose(short code, String reason) {
        logger.debug("WsHiXie75#onClose cid:" + handler.getChannelId());
        callOnWsClose(code, reason);
        if (code != WsHybiFrame.CLOSE_UNKOWN) {
            handler.asyncClose(null);
        }
    }

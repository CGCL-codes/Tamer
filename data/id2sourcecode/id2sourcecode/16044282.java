    @Override
    public boolean onHandshake(HeaderParser requestHeader, String subProtocol) {
        logger.debug("WsHiXie76#onHandshake cid:" + handler.getChannelId());
        this.subProtocol = subProtocol;
        ByteBuffer[] body = requestHeader.getBodyBuffer();
        if (wsShakehand(requestHeader, body)) {
            return true;
        }
        return false;
    }

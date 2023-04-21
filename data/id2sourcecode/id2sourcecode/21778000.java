    public void onReadPlain(Object userContext, ByteBuffer[] buffers) {
        logger.debug("#readPlain.cid:" + getChannelId());
        if (userContext == CONTEXT_MESSAGE) {
            parseFrame(buffers);
            asyncRead(CONTEXT_MESSAGE);
            return;
        }
        stat = STAT_MESSAGE;
        for (int i = 0; i < buffers.length; i++) {
            responseHeader.parse(buffers[i]);
        }
        PoolManager.poolArrayInstance(buffers);
        if (!responseHeader.isParseEnd()) {
            logger.debug("asyncRead(CONTEXT_HEADER) cid:" + getChannelId());
            asyncRead(CONTEXT_HEADER);
            return;
        }
        if (responseHeader.isParseError()) {
            logger.warn("http header error");
            doEndWsClientFailure(stat, FAILURE_PROTOCOL);
            asyncClose(null);
            return;
        }
        String statusCode = responseHeader.getStatusCode();
        onWcResponseHeader(responseHeader);
        String headerKey = responseHeader.getHeader("Sec-WebSocket-Accept");
        if (!"101".equals(statusCode) || !acceptKey.equals(headerKey)) {
            logger.debug("WsClientHandler fail to handshake.statusCode:" + statusCode + " acceptKey:" + acceptKey + " headerKey:" + headerKey);
            doEndWsClientFailure(stat, FAILURE_PROTOCOL);
            asyncClose(null);
            return;
        }
        String subprotocol = responseHeader.getHeader("Sec-WebSocket-Protocol");
        onWcHandshaked(subprotocol);
        ByteBuffer[] body = responseHeader.getBodyBuffer();
        if (body != null) {
            parseFrame(body);
        }
        setReadTimeout(0);
        logger.debug("asyncRead(CONTEXT_BODY) cid:" + getChannelId());
        asyncRead(CONTEXT_MESSAGE);
    }

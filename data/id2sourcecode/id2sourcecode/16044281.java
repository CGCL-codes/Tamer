    private boolean wsShakehand(HeaderParser requestHeader, ByteBuffer[] readBody) {
        logger.debug("WsHiXie76#wsShakehand cid:" + handler.getChannelId());
        if (!isUseSpec(SPEC)) {
            handler.completeResponse("400");
            return false;
        }
        handshakeStat = 0;
        handshakeBody = BuffersUtil.concatenate(handshakeBody, readBody);
        long bodyLength = BuffersUtil.remaining(handshakeBody);
        if (bodyLength > 8) {
            logger.error("too big body before handshake.bodyLength:" + bodyLength);
            handler.completeResponse("403");
            return false;
        } else if (bodyLength < 8) {
            handshakeStat = 1;
            handler.asyncRead(null);
            return true;
        }
        if (subProtocol != null) {
            handler.setHeader(SEC_WEBSOCKET_PROTOCOL, subProtocol);
        }
        String origin = requestHeader.getHeader("Origin");
        String host = requestHeader.getHeader(HeaderParser.HOST_HEADER);
        String path = requestHeader.getPath();
        handler.setHttpVersion("HTTP/1.1");
        handler.setStatusCode("101", "Web Socket Protocol Handshake");
        handler.setHeader("Upgrade", "WebSocket");
        handler.setHeader("Connection", "Upgrade");
        handler.setHeader("Sec-WebSocket-Origin", origin);
        StringBuilder sb = new StringBuilder();
        if (handler.isSsl()) {
            sb.append("wss://");
        } else {
            sb.append("ws://");
        }
        sb.append(host);
        sb.append(path);
        handler.setHeader("Sec-WebSocket-Location", sb.toString());
        handler.flushHeaderForWebSocket(SPEC, subProtocol);
        byte[] response = null;
        String key1 = requestHeader.getHeader(SEC_WEBSOCKET_KEY1);
        String key2 = requestHeader.getHeader(SEC_WEBSOCKET_KEY2);
        response = responseDigest(key1, key2, handshakeBody);
        PoolManager.poolBufferInstance(handshakeBody);
        handshakeBody = null;
        handler.asyncWrite(null, BuffersUtil.toByteBufferArray(ByteBuffer.wrap(response)));
        handshakeStat = 2;
        frameMode = FRAME_MODE_END;
        handler.onWsOpen(subProtocol);
        handler.setReadTimeout(0);
        handler.asyncRead(null);
        return true;
    }

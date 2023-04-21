    @Override
    public boolean onHandshake(HeaderParser requestHeader, String subProtocol) {
        logger.debug("WsHiXie75#onHandshake cid:" + handler.getChannelId());
        if (!isUseSpec(SPEC)) {
            handler.completeResponse("400");
            return false;
        }
        String origin = requestHeader.getHeader("Origin");
        String host = requestHeader.getHeader(HeaderParser.HOST_HEADER);
        String path = requestHeader.getPath();
        handler.setHttpVersion("HTTP/1.1");
        handler.setStatusCode("101", "Web Socket Protocol Handshake\r\nUpgrade: WebSocket\r\nConnection: Upgrade");
        handler.setHeader(WEBSOCKET_ORIGIN, origin);
        StringBuilder sb = new StringBuilder();
        if (handler.isSsl()) {
            sb.append("wss://");
        } else {
            sb.append("ws://");
        }
        sb.append(host);
        sb.append(path);
        handler.setHeader("WebSocket-Location", sb.toString());
        if (subProtocol != null) {
            handler.setHeader(WEBSOCKET_PROTOCOL, subProtocol);
        }
        handler.flushHeaderForWebSocket(SPEC, subProtocol);
        frameMode = FRAME_MODE_END;
        handler.onWsOpen(subProtocol);
        handler.setReadTimeout(0);
        handler.asyncRead(null);
        return true;
    }

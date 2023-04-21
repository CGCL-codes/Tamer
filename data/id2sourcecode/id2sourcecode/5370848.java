    @Override
    public boolean onHandshake(HeaderParser requestHeader, String subProtocol) {
        logger.debug("WsHybi10#onHandshake cid:" + handler.getChannelId());
        if (!isUseSpec(SPEC)) {
            handler.completeResponse("400");
            return false;
        }
        String version = requestHeader.getHeader(SEC_WEBSOCKET_VERSION);
        logger.debug("WebSocket version:" + version);
        int v = Integer.parseInt(version);
        if (v > SUPPORT_VERSION) {
            logger.debug("WsHybi10#version error:" + v);
            handler.setHeader(SEC_WEBSOCKET_VERSION, Integer.toString(SUPPORT_VERSION));
            handler.completeResponse("400");
            return false;
        }
        if (subProtocol != null) {
            handler.setHeader(SEC_WEBSOCKET_PROTOCOL, subProtocol);
        }
        String key = requestHeader.getHeader(SEC_WEBSOCKET_KEY);
        String accept = DataUtil.digestBase64Sha1((key + GUID).getBytes());
        handler.setHttpVersion("HTTP/1.1");
        handler.setStatusCode("101", "Switching Protocols");
        handler.setHeader("Upgrade", "WebSocket");
        handler.setHeader("Connection", "Upgrade");
        handler.setHeader(SEC_WEBSOCKET_ACCEPT, accept);
        handler.flushHeaderForWebSocket(SPEC, subProtocol);
        handler.onWsOpen(subProtocol);
        handler.setReadTimeout(getWebSocketPingInterval());
        handler.asyncRead(null);
        return true;
    }

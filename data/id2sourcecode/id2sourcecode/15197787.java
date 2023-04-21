    private WebSocketConnector initializeConnector(ChannelHandlerContext aCtx, HttpRequest aReq) {
        RequestHeader lHeader = getRequestHeader(aReq);
        int lSessionTimeout = lHeader.getTimeout(JWebSocketCommonConstants.DEFAULT_TIMEOUT);
        if (lSessionTimeout > 0) {
            aCtx.getChannel().getConfig().setConnectTimeoutMillis(lSessionTimeout);
        }
        WebSocketConnector lConnector = new NettyConnector(mEngine, this);
        lConnector.setHeader(lHeader);
        mEngine.getConnectors().put(lConnector.getId(), lConnector);
        lConnector.startConnector();
        mEngine.connectorStarted(lConnector);
        return lConnector;
    }

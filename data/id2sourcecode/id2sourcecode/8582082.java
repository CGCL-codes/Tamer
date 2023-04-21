    private void endOfResponse() {
        if (stat == STAT_END) {
            isKeepAlive = false;
        }
        if (isKeepAlive) {
            String connectionHeader = null;
            String httpVersion = responseHeader.getResHttpVersion();
            if (!webClientConnection.isHttps() && webClientConnection.isUseProxy()) {
                connectionHeader = responseHeader.getHeader(HeaderParser.PROXY_CONNECTION_HEADER);
            } else {
                connectionHeader = responseHeader.getHeader(HeaderParser.CONNECTION_HEADER);
            }
            if (HeaderParser.HTTP_VESION_10.equalsIgnoreCase(httpVersion)) {
                if (!HeaderParser.CONNECION_KEEP_ALIVE.equalsIgnoreCase(connectionHeader)) {
                    isKeepAlive = false;
                }
            }
            if (HeaderParser.HTTP_VESION_11.equalsIgnoreCase(httpVersion)) {
                if (HeaderParser.CONNECION_CLOSE.equalsIgnoreCase(connectionHeader)) {
                    isKeepAlive = false;
                }
            }
        }
        if (isKeepAlive == false) {
            asyncClose(null);
            return;
        }
        onRequestEnd(STAT_KEEP_ALIVE);
        if (requestHeaderBuffer != null) {
            PoolManager.poolBufferInstance(requestHeaderBuffer);
            requestHeaderBuffer = null;
        }
        if (requestBodyBuffer != null) {
            PoolManager.poolBufferInstance(requestBodyBuffer);
            requestBodyBuffer = null;
        }
        responseHeaderLength = requestHeaderLength = requestContentLength = requestContentWriteLength = 0;
        responseHeader.recycle();
        setReadTimeout(keepAliveTimeout);
        asyncRead(CONTEXT_HEADER);
        logger.debug("WebClientHandler keepAlive.cid:" + getChannelId());
    }

    private boolean doProxy() {
        logger.debug("#startResponse.cid:" + getChannelId());
        HeaderParser requestHeader = getRequestHeader();
        boolean isCallerKeepAlive = false;
        long keepAliveTimeout = 0;
        KeepAliveContext keepAliveContext = getKeepAliveContext();
        if (keepAliveContext != null) {
            isCallerKeepAlive = keepAliveContext.isKeepAlive();
            keepAliveTimeout = keepAliveContext.getKeepAliveTimeout();
        }
        calcResolveDigest(requestHeader);
        webClientHandler = getWebClientHandler(keepAliveContext, requestHeader);
        editRequestHeader(requestHeader);
        long connectTimeout = config.getConnectTimeout();
        boolean rc = webClientHandler.startRequest(this, null, connectTimeout, requestHeader, isCallerKeepAlive, keepAliveTimeout);
        if (rc == false) {
            completeResponse("500", "fail to request backserver");
            webClientHandler = null;
            return false;
        }
        logger.debug("client cid:" + getChannelId() + " server cid:" + webClientHandler.getChannelId());
        webClientHandler.setReadTimeout(getReadTimeout());
        webClientHandler.setWriteTimeout(getWriteTimeout());
        return true;
    }

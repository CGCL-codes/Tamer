    public final boolean startRequest(WsClient wsClient, Object userContext, long connectTimeout, String uri, String subProtocols, String Origin) {
        synchronized (this) {
            if (this.wsClient != null) {
                throw new IllegalStateException("aleardy had wsClient:" + this.wsClient);
            }
            setWsClient(wsClient);
            this.userContext = userContext;
        }
        this.requestHeaderBuffer = crateWsRequestHeader(webClientConnection, uri, subProtocols, Origin);
        Throwable error;
        if (stat == STAT_INIT) {
            synchronized (this) {
                if (asyncConnect(this, webClientConnection.getRemoteServer(), webClientConnection.getRemotePort(), connectTimeout)) {
                    stat = STAT_CONNECT;
                    setReadTimeout(config.getReadTimeout());
                    return true;
                }
            }
            logger.warn("fail to asyncConnect.");
            error = FAILURE_CONNECT;
        } else {
            logger.error("fail to doRequest.cid=" + getChannelId() + ":stat:" + stat);
            error = new Throwable("fail to doRequest.cid=" + getChannelId() + ":stat:" + stat);
        }
        TimerManager.setTimeout(0L, this, error);
        return false;
    }

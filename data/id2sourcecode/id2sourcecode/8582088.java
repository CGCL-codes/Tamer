    public final boolean startRequest(WebClient webClient, Object userContext, long connectTimeout, ByteBuffer[] requestHeaderBuffer, long requestContentLength, boolean isCallerkeepAlive, long keepAliveTimeout) {
        synchronized (this) {
            if (this.webClient != null) {
                throw new IllegalStateException("aleardy had webClient:" + this.webClient);
            }
            setWebClient(webClient);
            this.userContext = userContext;
        }
        this.gzipContext.recycle();
        this.isCallerKeepAlive = isCallerkeepAlive;
        this.keepAliveTimeout = keepAliveTimeout;
        this.isKeepAlive = isCallerKeepAlive;
        this.requestHeaderBuffer = requestHeaderBuffer;
        this.requestContentLength = requestContentLength;
        Throwable error;
        if (stat == STAT_KEEP_ALIVE) {
            setReadTimeout(config.getReadTimeout());
            internalStartRequest();
            return true;
        } else if (stat == STAT_INIT) {
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

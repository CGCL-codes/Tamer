    private void doEndWsClientFailure(int stat, Throwable t) {
        logger.debug("#requestFailure cid:" + getChannelId());
        synchronized (this) {
            this.stat = STAT_END;
            if (wsClient == null) {
                return;
            }
            WsClient wkWebClient = wsClient;
            Object wkUserContext = userContext;
            setWsClient(null);
            userContext = null;
            wkWebClient.onWcFailure(wkUserContext, stat, t);
        }
        if (t == FAILURE_CONNECT) {
            logger.warn("#requestFailure.connect failure");
        } else {
            logger.warn("#requestFailure.", t);
        }
    }

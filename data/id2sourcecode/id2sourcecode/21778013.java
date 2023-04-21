    private synchronized void doEndWsClient(short closeCode, String closeReason) {
        logger.debug("#endWsClient cid:" + getChannelId() + ":wsClient:" + wsClient);
        int lastStat = this.stat;
        this.stat = STAT_END;
        if (wsClient == null) {
            return;
        }
        WsClient wkWebClient = wsClient;
        Object wkUserContext = userContext;
        setWsClient(null);
        userContext = null;
        wkWebClient.onWcClose(wkUserContext, lastStat, closeCode, closeReason);
    }

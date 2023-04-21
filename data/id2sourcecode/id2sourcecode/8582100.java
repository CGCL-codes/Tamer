    private synchronized void onRequestEnd(int stat) {
        logger.debug("#requestEnd cid:" + getChannelId() + ":webClient:" + webClient);
        int lastStat = this.stat;
        this.stat = stat;
        if (webClient == null) {
            return;
        }
        WebClient wkWebClient = webClient;
        Object wkUserContext = userContext;
        setWebClient(null);
        userContext = null;
        wkWebClient.onRequestEnd(wkUserContext, lastStat);
    }

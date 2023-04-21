    private void onRequestFailure(int stat, Throwable t) {
        logger.debug("#requestFailure cid:" + getChannelId());
        synchronized (this) {
            if (webClient == null) {
                return;
            }
            WebClient wkWebClient = webClient;
            Object wkUserContext = userContext;
            setWebClient(null);
            userContext = null;
            wkWebClient.onRequestFailure(wkUserContext, stat, t);
        }
        if (t == FAILURE_CONNECT) {
            logger.warn("#requestFailure.connect failure");
        } else {
            logger.warn("#requestFailure.", t);
        }
    }

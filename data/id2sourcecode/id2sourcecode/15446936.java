    public void onRequestFailure(Object userContext, int stat, Throwable t) {
        logger.debug("#webClientFailure.cid:" + getChannelId() + ":" + stat, t);
        String statusCode = getResponseStatusCode();
        if (statusCode != null) {
            responseEnd();
        } else {
            completeResponse("500", "proxyHandler error.stat:" + stat);
        }
    }

    public void onRequestEnd(Object userContext, int stat) {
        logger.debug("#onRequestEnd:" + browserName);
        if (accessLog.getStatusCode() == null) {
            accessLog.setStatusCode("%" + Integer.toHexString(stat));
        }
        WebClientHandler webClientHandler = (WebClientHandler) userContext;
        accessLog.setRawRead(webClientHandler.getTotalWriteLength());
        accessLog.setRawWrite(webClientHandler.getTotalReadLength());
        accessLog.setChannelId(webClientHandler.getChannelId());
        accessLog.setResponseLength(responseLength);
        if (responseLength == 0) {
        } else if (responseBodyStore == null) {
            accessLog.setResponseBodyDigest(DataUtil.digest(messageDigest));
        } else {
            accessLog.incTrace();
            responseBodyStore.close(accessLog, responseBodyStore);
            accessLog.setResponseBodyDigest(responseBodyStore.getDigest());
            responseBodyStore = null;
        }
        accessLog.setRequestHeaderLength(webClientHandler.getRequestHeaderLength());
        accessLog.setResponseHeaderLength(webClientHandler.getResponseHeaderLength());
        accessLog.endProcess();
        AccessLog wkAccessLog = accessLog;
        WebClientLog webClientLog = accessLog.getWebClientLog();
        accessLog = null;
        if (webClientLog != null) {
            webClientLog.checkPoing(WebClientLog.CHECK_POINT_RESPONSE_BODY, webClientHandler.getTotalReadLength(), webClientHandler.getTotalWriteLength());
        }
        if (browser != null) {
            browser.onRequestEnd(this, wkAccessLog);
        }
    }

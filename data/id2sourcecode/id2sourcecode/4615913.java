    private void forwardMapping(String realHostName, HeaderParser requestHeader, MappingResult mapping, AuthSession auth, boolean isWs) {
        User user = null;
        if (auth != null) {
            user = auth.getUser();
        }
        setRequestAttribute(ServerBaseHandler.ATTRIBUTE_USER, user);
        setupTraceLog(realHostName, requestHeader, mapping, user, isWs);
        setRequestMapping(mapping);
        Class<WebServerHandler> responseClass = mapping.getHandlerClass();
        WebServerHandler response = (WebServerHandler) forwardHandler(responseClass);
        if (response == null) {
            logger.warn("fail to forwardHandler:cid:" + getChannelId() + ":" + this);
            return;
        }
        logger.debug("responseObject:cid:" + getChannelId() + ":" + response + ":" + this);
        response.startResponse();
    }

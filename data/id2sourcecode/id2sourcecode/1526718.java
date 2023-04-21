    public ChannelHandler forwardHandler(Class handlerClass, boolean callStartMethod) {
        logger.debug("#forwardHandler cid:" + getChannelId() + ":" + handlerClass.getName());
        WebServerHandler handler = (WebServerHandler) super.allocHandler(handlerClass);
        handler.responseHeader.setAllHeaders(responseHeader);
        handler.requestContentLength = requestContentLength;
        handler.requestReadBody = requestReadBody;
        super.forwardHandler(handler);
        if (callStartMethod) {
            if (handler.requestContentLength > 0 && handler.requestContentLength <= handler.requestReadBody) {
                handler.startResponseReqBody();
            } else {
                handler.startResponse();
            }
        }
        return handler;
    }

    public void onClosed(Object userContext) {
        logger.debug("#close client.id:" + getChannelId());
        if (requestDecodeHeader.isParseEnd()) {
            AccessLog accessLog = getAccessLog();
            String requestLine = accessLog.getRequestLine();
            if (requestLine == null) {
                logger.error("onClosed requestLine is null");
                requestLine = "[null]";
            }
            StringBuffer sb = new StringBuffer(requestLine);
            sb.append("[");
            sb.append(requestDecodeHeader.getRequestLine());
            sb.append("]");
            accessLog.setRequestLine(sb.toString());
            requestDecodeHeader.recycle();
        }
        super.onClosed(userContext);
        server.asyncClose(null);
    }

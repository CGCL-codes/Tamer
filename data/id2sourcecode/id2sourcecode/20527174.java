    public void log(boolean debug) {
        if (!debug && isSkipPhlog) {
            return;
        }
        StringBuffer sb = new StringBuffer(256);
        if (startTime != null) {
            sb.append(fomatLogDate(startTime));
        } else {
            sb.append(fomatLogDate(new Date(0)));
        }
        sb.append(" ");
        if (ip != null) {
            sb.append(ip);
        } else {
            sb.append("-");
        }
        sb.append(" ");
        if (userId != null) {
            sb.append(userId);
        } else {
            sb.append("-");
        }
        sb.append(" \"");
        sb.append(requestLine);
        sb.append("\" ");
        sb.append(statusCode);
        sb.append(" ");
        sb.append(responseLength);
        sb.append(" ");
        sb.append(processTime);
        if (!debug && !isShortFormat) {
            sb.append("#");
            sb.append(getRealHost());
            sb.append(",");
            switch(getSourceType()) {
                case SOURCE_TYPE_PLAIN_WEB:
                    sb.append("plainWeb");
                    break;
                case SOURCE_TYPE_SSL_WEB:
                    sb.append("sslWeb");
                    break;
                case SOURCE_TYPE_PLAIN_PROXY:
                    sb.append("plainProxy");
                    break;
                case SOURCE_TYPE_SSL_PROXY:
                    sb.append("sslProxy");
                    break;
                case SOURCE_TYPE_WS:
                    sb.append("ws");
                    break;
                case SOURCE_TYPE_WSS:
                    sb.append("wss");
                    break;
                case SOURCE_TYPE_WS_HANDSHAKE:
                    sb.append("wsHandshake");
                    break;
                case SOURCE_TYPE_WS_ON_MESSAGE:
                    sb.append("wsOnMessage");
                    break;
                case SOURCE_TYPE_WS_POST_MESSAGE:
                    sb.append("wsPostMessage");
                    break;
                case SOURCE_TYPE_SIMULATE:
                    sb.append("simulate");
                    break;
                default:
                    sb.append("-");
            }
            sb.append(",");
            sb.append(getDestinationType());
            sb.append(",");
            sb.append(getContentEncoding());
            sb.append(",");
            sb.append(getTransferEncoding());
            sb.append(",");
            sb.append(getRequestHeaderTime());
            sb.append(",");
            sb.append(getRequestBodyTime());
            sb.append(",");
            sb.append(getResponseHeaderTime());
            sb.append(",");
            sb.append(getResponseBodyTime());
            sb.append(",");
            sb.append(getChannelId());
        }
        String logText = sb.toString();
        if (debug) {
            logger.info(logText);
        } else {
            accesslogLogger.info(logText);
        }
    }

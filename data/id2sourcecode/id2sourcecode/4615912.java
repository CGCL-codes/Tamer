    private AccessLog setupTraceLog(String realHostName, HeaderParser requestHeader, MappingResult mapping, User user, boolean isWs) {
        AccessLog accessLog = getAccessLog();
        accessLog.setStartTime(startTime);
        accessLog.setConnectTime(connectTime);
        accessLog.setHandshakeTime(handshakeTime);
        accessLog.setTimeCheckPint(AccessLog.TimePoint.requestHeader);
        accessLog.setIp(getRemoteIp());
        if (user != null) {
            accessLog.setUserId(user.getLoginId());
        }
        if (Boolean.TRUE.equals(mapping.getOption("skipPhlog"))) {
            accessLog.setSkipPhlog(true);
        }
        if (Boolean.TRUE.equals(mapping.getOption("shortFormatLog"))) {
            accessLog.setShortFormat(true);
        }
        Mapping.SecureType secureType = mapping.getTargetSecureType();
        if (secureType != null) {
            switch(mapping.getTargetSecureType()) {
                case PLAIN:
                    if (mapping.isSourceTypeProxy()) {
                        accessLog.setSourceType(AccessLog.SOURCE_TYPE_PLAIN_PROXY);
                    } else if (isWs) {
                        accessLog.setSourceType(AccessLog.SOURCE_TYPE_WS);
                    } else {
                        accessLog.setSourceType(AccessLog.SOURCE_TYPE_PLAIN_WEB);
                    }
                    break;
                case SSL:
                    if (mapping.isSourceTypeProxy()) {
                        accessLog.setSourceType(AccessLog.SOURCE_TYPE_SSL_PROXY);
                    } else if (isWs) {
                        accessLog.setSourceType(AccessLog.SOURCE_TYPE_WSS);
                    } else {
                        accessLog.setSourceType(AccessLog.SOURCE_TYPE_SSL_WEB);
                    }
                    break;
            }
        }
        accessLog.setRealHost(realHostName);
        Mapping.DestinationType destinationType = mapping.getDestinationType();
        if (destinationType != null) {
            String origin = null;
            switch(mapping.getDestinationType()) {
                case HTTP:
                    accessLog.setDestinationType(AccessLog.DESTINATION_TYPE_HTTP);
                    origin = mapping.getResolveServer().toString();
                    break;
                case HTTPS:
                    accessLog.setDestinationType(AccessLog.DESTINATION_TYPE_HTTPS);
                    origin = mapping.getResolveServer().toString();
                    break;
                case FILE:
                    accessLog.setDestinationType(AccessLog.DESTINATION_TYPE_FILE);
                    origin = mapping.getDestinationFile().getAbsolutePath();
                    break;
                case HANDLER:
                    accessLog.setDestinationType(AccessLog.DESTINATION_TYPE_HANDLER);
                    origin = mapping.getHandlerClass().getName();
                    break;
                case WS:
                    accessLog.setDestinationType(AccessLog.DESTINATION_TYPE_WS);
                    origin = mapping.getResolveServer().toString();
                    break;
                case WSS:
                    accessLog.setDestinationType(AccessLog.DESTINATION_TYPE_WSS);
                    origin = mapping.getResolveServer().toString();
                    break;
            }
            accessLog.setResolveOrigin(origin);
        }
        accessLog.setRequestLine(requestHeader.getRequestLine());
        accessLog.setRequestHeaderLength(connectHeaderLength + requestHeader.getHeaderLength());
        accessLog.setChannelId(getChannelId());
        accessLog.setLocalIp(getLocalIp());
        logger.debug("cid:" + getChannelId() + ":requestLine:" + accessLog.getRequestLine());
        switch(mapping.getLogType()) {
            case NONE:
                headerPage.recycle();
                accessLog.setPersist(false);
                return accessLog;
            case TRACE:
            case REQUEST_TRACE:
                Store readPeekStore = Store.open(true);
                ByteBuffer[] buffers = headerPage.getBuffer();
                BuffersUtil.cut(buffers, connectHeaderLength + requestHeader.getHeaderLength());
                readPeekStore.putBuffer(buffers);
                logger.debug("#setupTraceLog" + readPeekStore.getStoreId());
                accessLog.incTrace();
                readPeekStore.close(accessLog, readPeekStore);
                accessLog.setRequestHeaderDigest(readPeekStore.getDigest());
                if (isWs) {
                    break;
                }
                readPeekStore = Store.open(true);
                ByteBuffer[] bufs = requestHeader.peekBodyBuffer();
                if (bufs != null) {
                    readPeekStore.putBuffer(bufs);
                }
                pushReadPeekStore(readPeekStore);
                break;
        }
        accessLog.setPersist(true);
        return accessLog;
    }

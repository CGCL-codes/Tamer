    public void flushHeaderForWebSocket(String spec, String subprotocol) {
        ByteBuffer[] headerBuffer = responseHeader.getHeaderBuffer();
        if (headerBuffer == null) {
            logger.warn("flushHeader fail to getHeaderBuffer.cid:" + getChannelId());
            asyncClose(null);
            return;
        }
        getAccessLog().setTimeCheckPint(AccessLog.TimePoint.responseHeader);
        responseHeaderLength = BuffersUtil.remaining(headerBuffer);
        Store responsePeek = null;
        MappingResult mapping = getRequestMapping();
        AccessLog accessLog = getAccessLog();
        if (mapping != null) {
            switch(mapping.getLogType()) {
                case RESPONSE_TRACE:
                case TRACE:
                    responsePeek = Store.open(true);
                    ByteBuffer[] headerDup = PoolManager.duplicateBuffers(headerBuffer);
                    responsePeek.putBuffer(headerDup);
                    logger.debug("#flushHeader" + responsePeek.getStoreId());
                    accessLog.incTrace();
                    responsePeek.close(accessLog, responsePeek);
                    accessLog.setResponseHeaderDigest(responsePeek.getDigest());
                case REQUEST_TRACE:
                case ACCESS:
                    AccessLog wsAccessLog = accessLog.copyForWs();
                    StringBuffer sb = new StringBuffer();
                    switch(mapping.getDestinationType()) {
                        case WS:
                            sb.append("ws://");
                            sb.append(mapping.getResolveServer());
                            sb.append(mapping.getResolvePath());
                            break;
                        case WSS:
                            sb.append("wss://");
                            sb.append(mapping.getResolveServer());
                            sb.append(mapping.getResolvePath());
                            break;
                        case HANDLER:
                            if (isSsl()) {
                                sb.append("wss://");
                            } else {
                                sb.append("ws://");
                            }
                            sb.append(config.getSelfDomain());
                            sb.append(':');
                            sb.append(config.getProperty(Config.SELF_PORT));
                            sb.append(mapping.getSourcePath());
                            break;
                    }
                    sb.append('[');
                    sb.append(spec);
                    sb.append(':');
                    if (subprotocol != null) {
                        sb.append(subprotocol);
                    }
                    sb.append(':');
                    sb.append(getChannelId());
                    sb.append(']');
                    wsAccessLog.setRequestLine(sb.toString());
                    wsAccessLog.setStatusCode("B=S");
                    wsAccessLog.endProcess();
                    wsAccessLog.setSourceType(AccessLog.SOURCE_TYPE_WS_HANDSHAKE);
                    wsAccessLog.setPersist(true);
                    wsAccessLog.decTrace();
            }
        }
        asyncWrite(WRITE_CONTEXT_HEADER, headerBuffer);
        isFlushFirstResponse = true;
        if (firstBody != null) {
            logger.error("flushHeader use only websocket.");
            asyncClose(null);
        }
    }

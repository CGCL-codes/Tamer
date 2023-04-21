    public void onConnected(Object userContext) {
        logger.debug("#connected.id:" + getChannelId());
        onWebConnected();
        if (webClientConnection.isHttps()) {
            if (webClientConnection.isUseProxy()) {
                stat = STAT_SSL_PROXY;
                StringBuffer sb = new StringBuffer(512);
                sb.append("CONNECT ");
                sb.append(webClientConnection.getTargetServer());
                sb.append(":");
                sb.append(webClientConnection.getTargetPort());
                sb.append(" HTTP/1.0\r\nHost: ");
                sb.append(webClientConnection.getTargetServer());
                sb.append(":");
                sb.append(webClientConnection.getTargetPort());
                sb.append("\r\nContent-Length: 0\r\n\r\n");
                ByteBuffer buf = ByteBuffer.wrap(sb.toString().getBytes());
                if (scheduler != null) {
                    scheduler.scheduleWrite(CONTEXT_SSL_PROXY_CONNECT, BuffersUtil.toByteBufferArray(buf));
                } else {
                    sslProxyActualWriteTime = System.currentTimeMillis();
                    asyncWrite(CONTEXT_SSL_PROXY_CONNECT, BuffersUtil.toByteBufferArray(buf));
                }
                asyncRead(CONTEXT_SSL_PROXY_CONNECT);
                return;
            } else {
                stat = STAT_SSL_HANDSHAKE;
                sslOpen(true);
                return;
            }
        }
        asyncRead(CONTEXT_HEADER);
        internalStartRequest();
    }

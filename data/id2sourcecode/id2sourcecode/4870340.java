        public void onRead(Object userContext, ByteBuffer[] buffers) {
            logger.debug("#read server.cid:" + getChannelId());
            if (isUseProxy && isProxyConnect == false) {
                for (int i = 0; i < buffers.length; i++) {
                    headerParser.parse(buffers[i]);
                }
                PoolManager.poolArrayInstance(buffers);
                if (headerParser.isParseEnd()) {
                    if (headerParser.isParseError()) {
                        logger.warn("ssl proxy header error");
                        client.completeResponse("500", "fail to ssl proxy connect");
                        return;
                    } else {
                        isProxyConnect = true;
                    }
                } else {
                    asyncRead(null);
                    return;
                }
                String statusCode = headerParser.getStatusCode();
                if (!"200".equals(statusCode)) {
                    client.completeResponse(statusCode, "fail to ssl proxy connect".getBytes());
                    return;
                }
                isHandshaked = false;
                sslOpenWithBuffer(true, buffers);
                return;
            }
            super.onRead(userContext, buffers);
        }

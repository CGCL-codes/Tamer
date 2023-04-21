        public void onConnected(Object userContext) {
            logger.debug("#connected server.id:" + getChannelId() + ":client id:" + client.getChannelId());
            isConnected = true;
            if (isUseProxy) {
                ByteBuffer[] headerBuffers = requestParser.getHeaderBuffer();
                asyncWrite(null, headerBuffers);
                isProxyConnect = false;
                asyncRead(null);
            } else {
                isHandshaked = false;
                sslOpen(true);
            }
        }

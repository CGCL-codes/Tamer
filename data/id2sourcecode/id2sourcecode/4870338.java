        public boolean onHandshaked() {
            logger.debug("#handshaked server.id:" + getChannelId());
            isHandshaked = true;
            client.setStatusCode("200");
            client.asyncWrite(SSL_PROXY_OK_CONTEXT, BuffersUtil.toByteBufferArray(ByteBuffer.wrap(ProxyOkResponse)));
            return false;
        }

        public void onTimeout(Object userContext) {
            logger.debug("#timeout server.id:" + getChannelId());
            if (userContext == SslAdapter.SSLCTX_READ_NETWORK) {
                long now = System.currentTimeMillis();
                if ((now - lastIo) < readTimeout) {
                    server.asyncRead(userContext);
                    return;
                }
            }
            logger.warn("server timeout." + userContext);
            server.asyncClose(userContext);
        }

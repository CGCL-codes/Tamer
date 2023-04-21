    public void onTimeout(Object userContext) {
        logger.debug("#timeout client.id:" + getChannelId());
        if (userContext == SslAdapter.SSLCTX_READ_NETWORK) {
            long now = System.currentTimeMillis();
            if ((now - lastIo) < readTimeout) {
                client.asyncRead(userContext);
                return;
            }
        }
        logger.warn("client timeout." + userContext);
        client.asyncClose(userContext);
    }

    public void onTimeout(Object userContext) {
        logger.debug("#timeout client.cid:" + client.getChannelId());
        if (userContext == READ_REQUEST) {
            long now = System.currentTimeMillis();
            if ((now - lastIo) < readTimeout) {
                client.asyncRead(READ_REQUEST);
                return;
            }
        }
        client.asyncClose(userContext);
    }

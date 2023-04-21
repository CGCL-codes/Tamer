        public void onTimeout(Object userContext) {
            logger.debug("#timeout.cid:" + getChannelId());
            if (userContext == READ_REQUEST) {
                long now = System.currentTimeMillis();
                if ((now - lastIo) < readTimeout) {
                    server.asyncRead(READ_REQUEST);
                    return;
                }
            }
            server.asyncClose(userContext);
        }

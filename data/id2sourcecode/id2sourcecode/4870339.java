        @Override
        public void onWrittenPlain(Object userContext) {
            logger.debug("#writtenPlain server.id:" + getChannelId());
            if (userContext == SSL_PROXY_OK_CONTEXT) {
                client.sslOpen(false);
            }
        }

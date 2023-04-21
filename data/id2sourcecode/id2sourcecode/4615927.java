    @Override
    public void onWrittenPlain(Object userContext) {
        logger.debug("#WrittenPlain cid:" + getChannelId());
        if (userContext == SSL_PROXY_OK_CONTEXT) {
            isFirstRead = true;
            asyncRead(null);
        }
    }

    public void onWrittenPlain(Object userContext) {
        logger.debug("#onWrittenPlain cid:" + getChannelId() + ":userContext:" + userContext);
        if (userContext == WRITE_CONTEXT_BODY) {
            onWrittenBody();
        }
        if (userContext == WRITE_CONTEXT_BODY || userContext == WRITE_CONTEXT_BODY_INTERNAL || userContext == WRITE_CONTEXT_LAST_HEADER) {
            synchronized (this) {
                if (getChannelId() < 0) {
                    return;
                }
                if (isResponseEnd) {
                    if (doneKeepAlive()) {
                        return;
                    }
                }
            }
        }
        super.onWrittenPlain(userContext);
    }

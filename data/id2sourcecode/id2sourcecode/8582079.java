    public final void onWrittenPlain(Object userContext) {
        logger.debug("#writtenPlain.cid:" + getChannelId());
        if (userContext == CONTEXT_HEADER) {
            onWrittenRequestHeader();
        } else if (userContext == CONTEXT_BODY) {
            onWrittenRequestBody();
        }
    }

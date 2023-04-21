    public final void onWrittenPlain(Object userContext) {
        logger.debug("#writtenPlain.cid:" + getChannelId());
        if (userContext == CONTEXT_HEADER) {
            onWcWrittenHeader();
        } else if (userContext == CONTEXT_MESSAGE) {
        }
    }

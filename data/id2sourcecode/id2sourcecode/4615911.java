    public void onReadPlain(Object userContext, ByteBuffer[] buffers) {
        logger.debug("#onReadPlain.cid:" + getChannelId() + ":buffers.hashCode:" + buffers.hashCode());
        if (startTime == null) {
            startTime = new Date();
        }
        headerPage.putBuffer(PoolManager.duplicateBuffers(buffers), true);
        HeaderParser headerParser = getRequestHeader();
        for (int i = 0; i < buffers.length; i++) {
            headerParser.parse(buffers[i]);
        }
        PoolManager.poolArrayInstance(buffers);
        if (headerParser.isParseEnd()) {
            if (headerParser.isParseError()) {
                logger.warn("http header error");
                asyncClose(null);
            } else {
                if (headerParser.isProxy() && getConfig().getRealHost(headerParser.getServer()) != null) {
                    headerParser.forceWebRequest();
                }
                mappingHandler();
            }
        } else {
            if (getLimitRequestFieldSize() <= headerPage.getBufferLength()) {
                logger.warn("too long header size." + headerPage.getBufferLength());
                asyncClose(null);
                return;
            }
            asyncRead(null);
        }
    }

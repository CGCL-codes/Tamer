    public void onRead(Object userContext, ByteBuffer[] buffers) {
        logger.debug("#onRead.cid:" + getChannelId() + ":buffers.hashCode:" + buffers.hashCode());
        if (isFirstRead) {
            if (startTime == null) {
                startTime = new Date();
            }
            connectTime = System.currentTimeMillis() - startTime.getTime();
            isFirstRead = false;
            setReadTimeout(getConfig().getReadTimeout());
            if (SslAdapter.isSsl(buffers[0])) {
                if (!sslOpenWithBuffer(false, buffers)) {
                    asyncClose(null);
                }
                return;
            }
        }
        super.onRead(userContext, buffers);
    }

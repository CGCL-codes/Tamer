    public void onStartRequest() {
        logger.debug("#startRequest.cid:" + getChannelId());
        headerPage.recycle();
        startTotalReadLength = getTotalReadLength();
        startTotalWriteLength = getTotalWriteLength();
        asyncRead(null);
    }

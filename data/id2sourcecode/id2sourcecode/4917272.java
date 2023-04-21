    @Override
    public void onWcClose(Object userContext, int stat, short closeCode, String closeReason) {
        logger.debug("#onWcClose cid:" + getChannelId());
        closeWebSocket("500", closeCode, closeReason);
        unref();
    }

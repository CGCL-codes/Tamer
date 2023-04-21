    @Override
    public void onMessage(CacheBuffer msgs) {
        logger.debug("#message bin cid:" + getChannelId());
        if (msgs instanceof PoolBase) {
            ((PoolBase) msgs).unref();
        }
    }

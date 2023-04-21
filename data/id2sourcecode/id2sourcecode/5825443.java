    @Override
    public void onMessage(String msgs) {
        logger.debug("#message text cid:" + getChannelId());
        if ("doClose".equals(msgs)) {
            closeWebSocket("500");
        } else {
            postMessage(msgs);
        }
    }

    @Override
    protected MessageEvent preProcessForwardMessageEvent(MessageEvent event) {
        if (event.getMessage() instanceof HttpResponse) {
            HttpResponse res = (HttpResponse) event.getMessage();
            if (logger.isDebugEnabled()) {
                logger.debug("Recv response:" + res);
            }
            ChannelBuffer content = res.getContent();
            if (res.getStatus().getCode() >= 300) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Recv error content:" + new String(content.toByteBuffer().array()));
                }
                return event;
            }
            if (content != null) {
                content = decrypt(content);
                return new UpstreamMessageEvent(event.getChannel(), content, event.getRemoteAddress());
            }
        } else if (event.getMessage() instanceof HttpChunk) {
            HttpChunk chunk = (HttpChunk) event.getMessage();
            ChannelBuffer content = chunk.getContent();
            return new UpstreamMessageEvent(event.getChannel(), decrypt(content), event.getRemoteAddress());
        }
        return event;
    }

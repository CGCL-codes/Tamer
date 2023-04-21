    void send(final Buffer content) {
        if (logger.isDebugEnabled()) {
            logger.debug("#####Push " + content.readableBytes());
        }
        ChannelFuture future = getRemoteFuture();
        if (future.getChannel().isConnected()) {
            sendContent(content);
        } else {
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        sendContent(content);
                    } else {
                        send(content);
                    }
                }
            });
        }
    }

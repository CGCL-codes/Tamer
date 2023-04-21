    protected boolean doSend(final Buffer content) {
        lastWriteTime = System.currentTimeMillis();
        ChannelFuture remote = getRemoteChannelFuture();
        if (remote.getChannel().isConnected()) {
            if (logger.isTraceEnabled()) {
                logger.trace("Reuse connected HTTP client channel.");
            }
            sendContent(remote.getChannel(), content);
        } else {
            remote.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        sendContent(future.getChannel(), content);
                        connectFailedCount = 0;
                    } else {
                        connectFailedCount++;
                        if (connectFailedCount < 3) {
                            logger.error("Failed to connect remote c4 server, try again");
                            doSend(content);
                        } else {
                            connectFailedCount = 0;
                        }
                    }
                }
            });
        }
        return true;
    }

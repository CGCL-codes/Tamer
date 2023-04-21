    protected void closeRemote() {
        if (logger.isDebugEnabled()) {
            logger.debug("Session[" + getID() + "] close all remote connection");
        }
        for (Channel ch : channelTable.values()) {
            if (ch.isConnected()) {
            }
        }
        channelTable.clear();
        if (null != currentChannelFuture && currentChannelFuture.getChannel().isConnected()) {
            if (isHttps) {
                currentChannelFuture.getChannel().close();
            } else {
                DirectRemoteChannelResponseHandler handler = currentChannelFuture.getChannel().getPipeline().get(DirectRemoteChannelResponseHandler.class);
                if (handler.unanwsered) {
                    currentChannelFuture.getChannel().close();
                }
            }
        }
    }

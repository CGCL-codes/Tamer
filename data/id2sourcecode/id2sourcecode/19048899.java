    protected void closeRemote() {
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

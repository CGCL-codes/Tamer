    protected void doClose() {
        if (clientChannelFuture != null && clientChannelFuture.getChannel().isConnected()) {
            clientChannelFuture.getChannel().close();
        }
        clientChannelFuture = null;
    }

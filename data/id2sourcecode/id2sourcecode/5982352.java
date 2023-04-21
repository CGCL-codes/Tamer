    @Override
    public void stopConnector(CloseReason aCloseReason) {
        if (log.isDebugEnabled()) {
            log.debug("Stopping Netty connector (" + aCloseReason.name() + ")...");
        }
        getEngine().connectorStopped(this, aCloseReason);
        if (handler.getChannelHandlerContext().getChannel().isConnected() && getEngine().isAlive()) {
            handler.getChannelHandlerContext().getChannel().close();
        }
        if (log.isInfoEnabled()) {
            log.info("Stopped Netty connector (" + aCloseReason.name() + ") on port " + getRemotePort());
        }
    }

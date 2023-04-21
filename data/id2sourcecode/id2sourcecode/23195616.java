    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        log.trace("Channel connected " + e.getChannel().getId());
        if (this.driver.listener != null) {
            driver.listener.onConnect();
        }
    }

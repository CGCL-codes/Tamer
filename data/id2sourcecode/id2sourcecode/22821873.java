    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.getChannel().setReadable(false);
        if (this.sessionMonitor != null) {
            this.sessionMonitor.setMonitoredChannel(ctx.getChannel());
            this.sessionMonitor.startMonitoring();
        }
        super.channelConnected(ctx, e);
        ctx.getChannel().setReadable(true);
    }

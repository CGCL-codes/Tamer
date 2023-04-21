    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.setAttachment(Boolean.TRUE);
        ctx.getChannel().setReadable(false);
        if (trafficCounter == null) {
            trafficCounter = new TrafficCounter(this, executor, "ChannelTC" + ctx.getChannel().getId(), checkInterval);
        }
        if (trafficCounter != null) {
            trafficCounter.start();
        }
        super.channelConnected(ctx, e);
        ctx.setAttachment(null);
        ctx.getChannel().setReadable(true);
    }

    @Override
    public void channelOpen(final ChannelHandlerContext channelHandlerContext, final ChannelStateEvent channelStateEvent) throws Exception {
        totalConnectionCount.incrementAndGet();
        currentConnectionCount.incrementAndGet();
        channelGroup.add(channelHandlerContext.getChannel());
    }

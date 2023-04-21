    @Override
    public void channelClosed(ChannelHandlerContext channelHandlerContext, ChannelStateEvent channelStateEvent) throws Exception {
        StatsCounter.curr_conns.decrementAndGet();
        channelGroup.remove(channelHandlerContext.getChannel());
    }

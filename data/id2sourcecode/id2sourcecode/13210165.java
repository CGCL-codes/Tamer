    @Override
    public void channelOpen(ChannelHandlerContext channelHandlerContext, ChannelStateEvent channelStateEvent) throws Exception {
        StatsCounter.total_conns.incrementAndGet();
        StatsCounter.curr_conns.incrementAndGet();
        channelGroup.add(channelHandlerContext.getChannel());
    }

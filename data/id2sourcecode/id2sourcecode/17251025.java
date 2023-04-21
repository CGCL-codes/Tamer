    @Override
    public void channelClosed(final ChannelHandlerContext channelHandlerContext, final ChannelStateEvent channelStateEvent) throws Exception {
        currentConnectionCount.decrementAndGet();
        channelGroup.remove(channelHandlerContext.getChannel());
    }

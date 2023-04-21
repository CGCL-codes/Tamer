    @Override
    public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        synchronized (trafficLock) {
            if (e.getChannel().isWritable()) {
                outboundChannel.setReadable(true);
            }
        }
    }

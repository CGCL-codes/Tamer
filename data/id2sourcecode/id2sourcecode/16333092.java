    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        super.handleUpstream(ctx, e);
        if (e instanceof ChannelStateEvent) {
            ChannelStateEvent evt = (ChannelStateEvent) e;
            if (evt.getState() == ChannelState.CONNECTED) {
                if (evt.getValue() == null) {
                    if (isBlocked(ctx)) {
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) e.getChannel().getRemoteAddress();
                        connectedSet.remove(inetSocketAddress.getAddress());
                    }
                }
            }
        }
    }

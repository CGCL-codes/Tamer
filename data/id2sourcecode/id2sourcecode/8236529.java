    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent evt) throws Exception {
        super.handleUpstream(ctx, evt);
        if (evt instanceof ChannelStateEvent) {
            ChannelStateEvent event = (ChannelStateEvent) evt;
            if (event.getState() == ChannelState.CONNECTED && event.getValue() != null && resetDelay != 0) {
                new Timer().schedule(new GenericProtocolDecoder.DisconnectTask(evt.getChannel()), resetDelay);
            }
        }
    }

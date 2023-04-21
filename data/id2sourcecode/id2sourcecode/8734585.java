    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        log.trace("Channel connected from " + e.getChannel().getRemoteAddress() + " is channel " + e.getChannel().getId());
        SessionContext session = new SessionContext(e.getChannel(), new JsonChannelWriter(e.getChannel()));
        this.onChannelConnected(e.getChannel(), session);
    }

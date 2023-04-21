    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Server.getAllChannels().add(e.getChannel());
        Server.getChannelsArray().put(e.getChannel().getId(), e.getChannel().getRemoteAddress().toString());
        Globals.getInstance().getServer().fireChannelOpen(ctx, e);
    }

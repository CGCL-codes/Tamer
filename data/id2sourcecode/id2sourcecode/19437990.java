    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Server.getAllChannels().remove(e.getChannel());
        Server.getChannelsArray().remove(e.getChannel().getId());
        Globals.getInstance().getServer().fireChannelClose(ctx, e);
    }

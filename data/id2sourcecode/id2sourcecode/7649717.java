    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
        NettyRPCServer.allChannels.add(e.getChannel());
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        authentHttp.getAuth().specialNoSessionAuth(false, Configuration.configuration.HOST_ID);
        super.channelConnected(ctx, e);
        ChannelGroup group = Configuration.configuration.getHttpChannelGroup();
        if (group != null) {
            group.add(e.getChannel());
        }
    }

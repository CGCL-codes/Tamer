    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        String ip = ((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();
        MUSLog.Log("Client connection initialized : " + (m_server.m_clientlist.size() + 1) + " (" + ip + ")", MUSLog.kSrv);
        MUSUser newUser = new MUSUser(m_server, ctx.getChannel());
        ((SMUSPipeline) ctx.getPipeline()).user = newUser;
    }

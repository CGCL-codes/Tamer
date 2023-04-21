    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        e.getChannel().write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        e.getChannel().write("It is " + new Date() + " now.\r\n");
    }

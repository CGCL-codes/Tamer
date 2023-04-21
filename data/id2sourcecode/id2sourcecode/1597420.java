    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("DNSClient#channelConnected");
        System.out.printf("Local %s | Remote %s\n", e.getChannel().getLocalAddress(), e.getChannel().getRemoteAddress());
        ChannelBuffer buffer = ChannelBuffers.buffer(512);
        this.request.write(buffer);
        this.time = System.currentTimeMillis();
        e.getChannel().write(buffer);
    }

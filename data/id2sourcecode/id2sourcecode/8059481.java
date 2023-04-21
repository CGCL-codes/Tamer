    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        localChannel = ctx.getChannel();
        if (null == remoteChannel) {
            ChannelPipeline pipeline = pipeline();
            pipeline.addLast("fr", new RemoteForwardLocalHandler());
            remoteChannel = factory.newChannel(pipeline);
            remoteChannel.connect(HttpsReverseServer.getInstance().getReverseServerSocketAddress()).awaitUninterruptibly();
        }
        Object mesage = e.getMessage();
        if (mesage instanceof ChannelBuffer) {
            ChannelBuffer bufmsg = (ChannelBuffer) mesage;
            remoteChannel.write(mesage);
        }
    }

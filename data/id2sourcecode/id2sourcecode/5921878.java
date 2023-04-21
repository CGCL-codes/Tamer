    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = e.getChannel();
        ChannelBuffer time = ChannelBuffers.buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000));
        ChannelFuture channelFuture = channel.write(time);
        channelFuture.addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture future) throws Exception {
                Channel ch = future.getChannel();
                ch.close();
            }
        });
    }

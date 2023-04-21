    @Override
    public void channelConnected(ChannelHandlerContext aContext, ChannelStateEvent aEvent) throws Exception {
        channel = aContext.getChannel();
        if (socketObserver != null) {
            socketObserver.onConnect();
        }
    }

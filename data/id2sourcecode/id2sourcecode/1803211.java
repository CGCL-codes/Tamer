    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Channel channel = HttpChannelAssociations.channels.get(e.getChannel());
        if (channel != null) {
            channel.write(e.getMessage());
        }
    }

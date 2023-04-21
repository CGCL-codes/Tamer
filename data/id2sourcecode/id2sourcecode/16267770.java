    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        this.channel = e.getChannel();
        e.getChannel().getRemoteAddress();
        this.channel.getPipeline().addFirst("encoder", new ObjectEncoder());
        this.channel.getPipeline().addFirst("decoder", new ObjectDecoder());
    }

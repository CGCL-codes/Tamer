    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        LOG.info("ProxyHandler#messageReceived");
        ChannelBuffer origBuffer = (ChannelBuffer) e.getMessage();
        this.original = new DNSMessage(origBuffer);
        this.inboundAddr = e.getRemoteAddress();
        this.inboundChannel = ctx.getChannel();
        LOG.info(this.original.header().toString());
        Channel ch = ctx.getChannel();
        LOG.info("addr : {} {}", e.getRemoteAddress(), ch.getLocalAddress());
        LOG.info("{}", this.original.header().toString());
        ChannelBuffer buffer = ChannelBuffers.buffer(512);
        DNSMessage newone = new DNSMessage(this.original);
        newone.write(buffer);
        this.outboundChannel.write(buffer);
    }

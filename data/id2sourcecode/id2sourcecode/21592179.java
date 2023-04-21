    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        if (!(msg instanceof ChannelBuffer) || !(readProtocolRequest || installProtocolDecoder)) {
            ctx.sendUpstream(e);
        }
        ChannelBuffer buf = (ChannelBuffer) msg;
        if (readProtocolRequest) {
            if (buf.readableBytes() == 0) {
                ctx.sendUpstream(e);
            }
            this.protocolType = ProtocolType.forCode(buf.readByte());
            this.readProtocolRequest = false;
            Channels.fireMessageReceived(ctx, ctx.getChannel(), this.protocolType);
        }
        if (installProtocolDecoder) {
            FrameDecoder decoder = getDecoderFor(this.protocolType);
            if (decoder == null) {
                throw new RuntimeException("No decoder available for protocol type '" + this.protocolType.getDescription() + "'.");
            }
            ctx.getPipeline().addAfter(ctx.getName(), ctx.getName() + "-subdecoder", decoder);
            this.installProtocolDecoder = false;
            Channels.fireMessageReceived(ctx, ctx.getChannel(), buf);
        }
    }

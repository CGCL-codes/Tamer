    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (!(e.getMessage() instanceof Message)) {
            ctx.sendDownstream(e);
            return;
        }
        Message m = (Message) e.getMessage();
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer(ChannelBuffers.LITTLE_ENDIAN, m.size());
        m.writeHeaderTo(buf);
        m.writeTo(buf);
        m.writeFooterTo(buf);
        buf.markReaderIndex();
        byte[] msg_bytes = new byte[buf.readableBytes()];
        buf.readBytes(msg_bytes);
        buf.resetReaderIndex();
        log.debug("Sending message:");
        Strings.prettyPrintHex(log, msg_bytes);
        int fullSlices = buf.readableBytes() / sliceLength;
        boolean exact = (fullSlices * sliceLength == buf.readableBytes());
        for (int i = 0; i < fullSlices; i++) {
            ChannelFuture future = (exact && (i == fullSlices - 1)) ? e.getFuture() : Channels.future(e.getChannel());
            Channels.write(ctx, e.getChannel(), future, buf.readSlice(sliceLength), e.getRemoteAddress());
        }
        if (!exact) {
            buf.discardReadBytes();
            Channels.write(ctx, e.getChannel(), e.getFuture(), buf, e.getRemoteAddress());
        }
    }

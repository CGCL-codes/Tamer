    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() < 2) {
            return null;
        }
        final int magic1 = buffer.getUnsignedByte(buffer.readerIndex());
        final int magic2 = buffer.getUnsignedByte(buffer.readerIndex() + 1);
        if (isSsl(magic1)) {
            enableSsl(ctx);
        } else if (isGzip(magic1, magic2)) {
            enableGzip(ctx);
        } else if (isHttp(magic1, magic2)) {
            switchToHttp(ctx);
        } else if (isFactorial(magic1)) {
            switchToFactorial(ctx);
        } else {
            buffer.skipBytes(buffer.readableBytes());
            ctx.getChannel().close();
            return null;
        }
        return buffer.readBytes(buffer.readableBytes());
    }

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            ChannelBuffer data = frame.getBinaryData();
            int firstByte = prepareFirstByte(frame);
            ChannelBuffer encoded = writeFundamentalData(data, channel, firstByte);
            encoded.writeBytes(data, data.readerIndex(), data.readableBytes());
            return encoded;
        }
        return msg;
    }

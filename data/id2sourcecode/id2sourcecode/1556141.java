    @Override
    public void sendPacket(WebsocketConnector connector, byte[] contentAsBytes) {
        Channel channel = ((NettyConnector) connector).getChannel();
        channel.write(new DefaultWebSocketFrame(OperationCodes.BINARY_FRAME_CODE.getCode(), ChannelBuffers.copiedBuffer(contentAsBytes)));
    }

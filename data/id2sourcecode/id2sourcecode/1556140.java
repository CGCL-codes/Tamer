    @Override
    public void sendPacket(WebsocketConnector connector, String contentAsString) {
        Channel channel = ((NettyConnector) connector).getChannel();
        if (connector.getWebsocketVersion().equals(WebsocketVersions.HYBI_13.getVersionCode())) {
            channel.write(new DefaultWebSocketFrame(OperationCodes.TEXT_FRAME_CODE.getCode(), ChannelBuffers.copiedBuffer(contentAsString, CharsetUtil.UTF_8)));
        } else {
            channel.write(new DefaultWebSocketFrame(contentAsString));
        }
    }

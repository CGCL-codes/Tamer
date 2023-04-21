    @Override
    public void sendPacket(WebSocketPacket aDataPacket) {
        if (handler.getChannelHandlerContext().getChannel().isConnected() && getEngine().isAlive()) {
            handler.getChannelHandlerContext().getChannel().write(new DefaultWebSocketFrame(aDataPacket.getString()));
        }
    }

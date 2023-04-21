    public IOFuture sendPacketAsync(WebSocketPacket aDataPacket) {
        if (handler.getChannelHandlerContext().getChannel().isConnected() && getEngine().isAlive()) {
            ChannelFuture internalFuture = handler.getChannelHandlerContext().getChannel().write(new DefaultWebSocketFrame(aDataPacket.getString()));
            return new NIOFuture(this, internalFuture);
        } else {
            return null;
        }
    }

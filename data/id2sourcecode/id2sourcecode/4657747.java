    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        ctx.getChannel().write(new DefaultWebSocketFrame(frame.getTextData().toUpperCase()));
    }

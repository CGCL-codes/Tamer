    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        ServerMessage req = (ServerMessage) e.getMessage();
        ClientMessage res = new ClientMessage("Echo: " + req.getPayload(), SystemColor.ERROR);
        ctx.getChannel().write(res);
    }

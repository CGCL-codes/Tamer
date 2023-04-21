    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        transferredMessages.incrementAndGet();
        e.getChannel().write(e.getMessage());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
        e.getChannel().write(e.getMessage());
    }

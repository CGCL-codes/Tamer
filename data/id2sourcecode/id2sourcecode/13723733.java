    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        if (msg instanceof ChannelBuffer) {
            ChannelBuffer buf = (ChannelBuffer) msg;
            byte[] bs = buf.array();
            System.out.println(new String(bs));
            ctx.getChannel().write(buf);
            e.getChannel().close();
        }
    }

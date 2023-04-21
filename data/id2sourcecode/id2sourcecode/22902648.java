    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String msg = (String) e.getMessage();
        if (msg.equals("QOTM?")) {
            e.getChannel().write("QOTM: " + nextQuote(), e.getRemoteAddress());
        }
    }

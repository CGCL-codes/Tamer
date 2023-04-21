    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String request = (String) e.getMessage();
        for (Channel c : channels) {
            if (c != e.getChannel()) {
                c.write("[" + e.getChannel().getRemoteAddress() + "] " + request + '\n');
            } else {
                c.write("[you] " + request + '\n');
            }
        }
        if (request.toLowerCase().equals("bye")) {
            e.getChannel().close();
        }
    }

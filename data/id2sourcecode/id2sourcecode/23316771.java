    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String request = (String) e.getMessage();
        String response;
        boolean close = false;
        if (request.length() == 0) {
            response = "Please type something.\r\n";
        } else if (request.toLowerCase().equals("bye")) {
            response = "Have a good day!\r\n";
            close = true;
        } else {
            response = "Did you say '" + request + "'?\r\n";
        }
        ChannelFuture future = e.getChannel().write(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

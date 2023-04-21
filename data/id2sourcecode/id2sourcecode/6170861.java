    @Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) {
        receivedMessages++;
        if (receivedMessages == count) {
            e.getChannel().close().addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture future) {
                    boolean offered = answer.offer((BigInteger) e.getMessage());
                    assert offered;
                }
            });
        }
    }

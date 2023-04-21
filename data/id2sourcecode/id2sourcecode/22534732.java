    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Channel channel = e.getChannel();
        if (e.getMessage() instanceof ChannelBuffer) {
            ChannelBuffer buf = (ChannelBuffer) e.getMessage();
            byte[] array = new byte[10];
            buf.readBytes(array);
            String id = new String(array);
            if (logger.isDebugEnabled()) {
                logger.debug("Recv a session id:" + id);
            }
            channel.getPipeline().remove("acceptor");
            if (table.containsKey(id)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Invoke callback by session id:" + id);
                }
                table.remove(id).callback(channel);
            }
        }
    }

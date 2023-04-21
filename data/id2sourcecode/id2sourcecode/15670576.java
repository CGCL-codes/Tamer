    @Override
    public final void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        ConnectionHandler p = (ConnectionHandler) ctx.getAttachment();
        ChannelBuffer buf = (ChannelBuffer) e.getMessage();
        buf.markReaderIndex();
        int avail = buf.readableBytes();
        if (avail > 5000) {
            e.getChannel().close();
            return;
        }
        byte[] b = new byte[avail];
        buf.readBytes(b);
        InStream in = new InStream(b);
        if (p.getPlayer() == null) {
            ConnectionWorker.run(p, in);
        } else {
            Packets.run(p, in);
        }
    }

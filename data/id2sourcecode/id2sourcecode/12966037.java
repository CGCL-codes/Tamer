            @Override
            public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
                ChannelBuffer buff = ChannelBuffers.directBuffer(10);
                buff.writeInt(500);
                e.getChannel().write(buff);
                e.getChannel().close();
            }

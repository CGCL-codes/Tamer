        @Override
        public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
            ChannelBuffer msg = (ChannelBuffer) e.getMessage();
            synchronized (locker) {
                inboundChannel.write(msg);
                if (!inboundChannel.isWritable()) {
                    e.getChannel().setReadable(false);
                }
            }
        }

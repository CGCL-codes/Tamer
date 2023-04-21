        @Override
        public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            synchronized (locker) {
                if (e.getChannel().isWritable()) {
                    inboundChannel.setReadable(true);
                }
            }
        }

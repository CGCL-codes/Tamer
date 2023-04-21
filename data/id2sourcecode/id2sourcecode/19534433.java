        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            logger.info("Connected to " + e.getChannel().getRemoteAddress().toString() + "!");
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Channel closed " + e);
            }
            allChannels.remove(ctx.getChannel());
        }

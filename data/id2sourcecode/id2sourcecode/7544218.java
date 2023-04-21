        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            addressTable.remove(e.getChannel());
            waitingReplyChannelSet.remove(e.getChannel());
            if (logger.isDebugEnabled()) {
                logger.debug("Connection closed.");
            }
        }

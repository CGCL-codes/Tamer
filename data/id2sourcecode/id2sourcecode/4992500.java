        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            if (LOG.isTraceEnabled()) {
                LOG.trace("message received called " + e.getMessage());
            }
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("New message " + e.toString() + " from " + ctx.getChannel());
                }
                NettyServerCnxn cnxn = (NettyServerCnxn) ctx.getAttachment();
                synchronized (cnxn) {
                    processMessage(e, cnxn);
                }
            } catch (Exception ex) {
                LOG.error("Unexpected exception in receive", ex);
                throw ex;
            }
        }

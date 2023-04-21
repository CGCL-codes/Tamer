        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Channel connected " + e);
            }
            allChannels.add(ctx.getChannel());
            NettyServerCnxn cnxn = new NettyServerCnxn(ctx.getChannel(), zkServer, NettyServerCnxnFactory.this);
            ctx.setAttachment(cnxn);
            addCnxn(cnxn);
        }

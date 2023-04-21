    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        SessionManager.logger.info("Connection from " + ctx.getChannel().getRemoteAddress().toString() + " closed.");
        if (this.context != null && this.handler != null) {
            this.handler.sessionEnded(this.context);
        }
    }

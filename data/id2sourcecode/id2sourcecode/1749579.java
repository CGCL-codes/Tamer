    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        SessionManager.logger.info("Received message of type " + msg.getClass().getName() + " from " + ctx.getChannel().getRemoteAddress().toString() + ".");
        if (this.protocol == null) {
            if (!(msg instanceof ProtocolType)) throw new IllegalStateException("Received message but protocol not yet specified.");
            this.protocol = (ProtocolType) msg;
            if (!this.sessionManager.sessionHandlers.containsKey(this.protocol)) throw new IllegalStateException("Protocol has no handler defined.");
            this.context = new SessionContext(ctx.getChannel());
            this.handler = this.sessionManager.getSessionHandler(this.protocol);
            this.handler.sessionStarted(this.context);
            return;
        }
        MessageHandler msgHandler = messageHandlers.get(msg.getClass());
        if (msgHandler == null) throw new IllegalArgumentException("No handler is registered for this message type.");
        msgHandler.handleMessage((Message) msg);
    }

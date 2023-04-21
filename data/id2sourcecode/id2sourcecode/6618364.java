        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            log.trace("Connected!");
            log.trace("Session state: " + si.state);
            synchronized (BattleNetChatClient.this) {
                if (si.state != SessionState.CONNECT_WAIT) {
                    AssertionError ae = new AssertionError();
                    log.throwing(ae);
                    throw ae;
                }
                si.sock = ctx.getChannel();
                nullTimer = new Timer();
                nullTimer.schedule(new KeepAliveTask(), KEEPALIVE_INTERVAL, KEEPALIVE_INTERVAL);
                beginHandshake();
            }
        }

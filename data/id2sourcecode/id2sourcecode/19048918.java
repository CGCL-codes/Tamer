        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            relaySession.closeRemote(ctx.getChannel(), remoteAddress);
            if (isHttps) {
                relaySession.closeLocalChannel();
            } else {
                if (unanwsered) {
                    relaySession.closeLocalChannel();
                }
            }
        }

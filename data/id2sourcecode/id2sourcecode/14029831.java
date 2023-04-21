        public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
            if (e instanceof MessageEvent) {
                final MessageEvent evt = (MessageEvent) e;
                String msg = (String) evt.getMessage();
                if (msg.equalsIgnoreCase("quit")) {
                    Channels.close(e.getChannel());
                    return;
                }
            }
            ctx.sendUpstream(e);
        }

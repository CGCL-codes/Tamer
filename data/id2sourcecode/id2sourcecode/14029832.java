        public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) {
            if (e instanceof MessageEvent) {
                final MessageEvent evt = (MessageEvent) e;
                String msg = (String) evt.getMessage();
                if (msg.equalsIgnoreCase("quit")) {
                    Channels.close(e.getChannel());
                    return;
                }
                System.err.println("SERVER:" + msg);
                Channels.write(e.getChannel(), msg);
            }
            ctx.sendDownstream(e);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            RouterMessage rm = (RouterMessage) e.getMessage();
            if (rm.hasExtension(TerminationNotice.extension)) {
                TerminationNotice tn = rm.getExtension(TerminationNotice.extension);
                termReason = tn.getReason();
                ctx.getChannel().close();
            } else {
                receiveMessage(link, rm);
            }
        }

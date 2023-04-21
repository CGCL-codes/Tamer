            public void ChannelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
                server.pnlSystemLog.addLog("A client: " + e.getChannel().getId() + " " + e.getChannel().getRemoteAddress().toString() + " connected.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldGreen, e.getChannel().getId().toString()));
            }

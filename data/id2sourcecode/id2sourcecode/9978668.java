            public void ChannelClose(ChannelHandlerContext ctx, ChannelStateEvent e) {
                server.pnlSystemLog.addLog("A client: " + e.getChannel().getId() + " " + e.getChannel().getRemoteAddress().toString() + " disconnected.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.boldOrange, e.getChannel().getId().toString()));
            }

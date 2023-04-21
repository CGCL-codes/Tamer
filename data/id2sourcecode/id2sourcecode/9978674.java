            public void ExceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
                server.pnlSystemLog.addLog("Error: " + e.getCause().getMessage(), new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error, e.getChannel().getId().toString()));
            }

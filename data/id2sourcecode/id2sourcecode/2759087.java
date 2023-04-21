    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String mesg = (String) e.getMessage();
        if (firstMessage) {
            firstMessage = false;
            int pos = mesg.indexOf(' ');
            try {
                result.status = Integer.parseInt(mesg.substring(0, pos));
            } catch (NumberFormatException e1) {
                result.set(LocalExecDefaultResult.BadTransmition);
                back.append(mesg);
                ctx.getChannel().close();
                return;
            }
            mesg = mesg.substring(pos + 1);
            result.result = mesg;
            back.append(mesg);
        } else if (LocalExecDefaultResult.ENDOFCOMMAND.startsWith(mesg)) {
            logger.debug("Receive End of Command");
            this.finalizeMessage();
        } else {
            back.append('\n');
            back.append(mesg);
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        e.getChannel().write(HumanReadableText.GREETINGS);
        ctx.setAttachment(new ImapSession());
    }

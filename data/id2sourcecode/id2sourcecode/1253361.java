    private void continueSet(Channel channel, SessionStatus state, byte[] remainder, ChannelHandlerContext channelHandlerContext) {
        state.cmd.element.setData(remainder);
        Channels.fireMessageReceived(channelHandlerContext, state.cmd, channelHandlerContext.getChannel().getRemoteAddress());
    }

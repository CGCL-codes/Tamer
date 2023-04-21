    private void checkChannelError(ServerResponseEvent<CubeIRC> event) {
        if (event.getCode() == ReplyConstants.ERR_INVITEONLYCHAN || event.getCode() == ReplyConstants.ERR_CHANNELISFULL || event.getCode() == ReplyConstants.ERR_NOPRIVILEGES) {
            String nick = event.getBot().getNick() + " ";
            String ev = event.getResponse().replace(nick, "");
            Channel ch = event.getBot().getChannel(ev.split(" :")[0]);
            String message = ev.split(" :")[1];
            MessageQueue.addQueue(MessageQueueEnum.IRC_ERROR_CHANNEL, new ChannelErrorResponse(event.getCode(), ch, message));
        }
    }

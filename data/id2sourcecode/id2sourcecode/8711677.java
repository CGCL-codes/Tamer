    private Event handleServerReplies(String textReply) {
        RplServerReply reply = RplFactory.getReply(textReply);
        if (reply == null) {
            return getDefaultEvent(textReply);
        }
        switch(reply.getReplyId()) {
            case RplServerReply.RPL_WELCOME:
                RplWelcome welcome = (RplWelcome) reply;
                return new ConnectedEvent(textReply);
            case RplServerReply.RPL_TOPIC:
                RplTopic topic = (RplTopic) reply;
                state.joinChannel(topic.getChannelName());
                return getDefaultEvent(textReply);
            case RplServerReply.RPL_NAMREPLY:
                RplNamReply nameReply = (RplNamReply) reply;
                Channel channel = state.joinChannel(nameReply.getChannelName());
                for (String name : nameReply.getNames()) {
                    channel.addMember(name);
                }
                return new NameReplyEvent(channel);
            default:
                return getDefaultEvent(textReply);
        }
    }

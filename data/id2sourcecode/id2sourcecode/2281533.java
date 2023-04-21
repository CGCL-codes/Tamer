    public IRCChannelParticipant getChannelRoleByChannelName(IRCUser user, String channelName) {
        return getChannelRoleByNickName(getChannelJoinedByChannelName(channelName), user.getActiveNick());
    }

    public IRCChannelParticipant getChannelRoleByNickName(IRCChannel ircChannel, String nick) {
        Set<RoomParticipant> set = ircChannel.getRoomParticipants();
        for (RoomParticipant participant : set) {
            IRCUser user = (IRCUser) participant.getUser();
            if (user.getActiveNick().equalsIgnoreCase(nick)) return (IRCChannelParticipant) participant;
        }
        return null;
    }

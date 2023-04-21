    protected static void handleJoin(IRCListener irclistener, IRCMessage ircmessage) throws IOException, ExpectException {
        IRCUser userJoined = ircmessage.getSender();
        String nickJoined = userJoined.getActiveNick();
        Lang.ASSERT_NOT_NULL_NOR_TRIMMED_EMPTY(nickJoined, "nickJoined");
        String channelName = ircmessage.getTrailing();
        if (channelName == null) channelName = ircmessage.getMiddlePart(0);
        if (channelName != null) channelName = channelName.trim();
        IRCController queryClient = irclistener.getProtocolHandler().getLocalClient();
        if (nickJoined.equalsIgnoreCase(irclistener.getProtocolHandler().getNick())) {
            queryClient.onMeJoinedChannel(channelName, userJoined);
            irclistener.join(channelName);
        } else {
            IRCChannel ch = queryClient.getChannelJoinedByChannelName(channelName);
            Lang.EXPECT_NOT_NULL(ch, "channel named " + StringUtil.toPrintableString(channelName));
            final RoomParticipant rp = queryClient.createDefaultRole(ch, userJoined);
            rp.getRoom().addRoomRole(rp);
            ch.joins((IRCChannelParticipant) rp);
            irclistener.join(channelName, userJoined);
        }
    }

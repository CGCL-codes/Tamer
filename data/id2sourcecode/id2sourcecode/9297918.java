    protected static void parseNamReply(IRCListener irclistener, IRCMessage ircmessage) throws IOException, ExpectException {
        String channelName = ircmessage.getMiddlePart(2);
        Lang.EXPECT_NOT_NULL_NOR_TRIMMED_EMPTY(channelName, "channelName");
        IRCController queryClient = irclistener.getProtocolHandler().getLocalClient();
        IRCChannel IRCChannel = queryClient.getChannelJoinedByChannelName(channelName);
        Lang.EXPECT_NOT_NULL(IRCChannel, "IRCChannel");
        irclistener.namReplyStart(channelName);
        String nickName;
        char modifierChar;
        StringTokenizer st = new StringTokenizer(ircmessage.getTrailing());
        while (st.hasMoreElements()) {
            nickName = st.nextToken();
            Lang.EXPECT_POSITIVE(nickName.length(), "@nick length for " + StringUtil.toPrintableString(nickName));
            modifierChar = nickName.charAt(0);
            if (modifierChar == '@' || modifierChar == '+') nickName = nickName.substring(1); else modifierChar = ' ';
            Lang.EXPECT_POSITIVE(nickName.length(), "nick length for " + StringUtil.toPrintableString(nickName));
            if (!nickName.equalsIgnoreCase(queryClient.getActiveNick())) {
            }
            irclistener.namReplyAddNick(nickName, modifierChar);
        }
        irclistener.namReplyFinish();
    }

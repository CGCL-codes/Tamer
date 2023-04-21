    protected static void handleCommand(int i, IRCMessage msg, IRCListener il) throws Exception {
        Lang.ASSERT_NOT_NULL(il, "il");
        Lang.ASSERT_NOT_NULL(msg, "msg");
        IRCController queryClient = il.getProtocolHandler().getLocalClient();
        Lang.ASSERT_NOT_NULL(queryClient, "queryClient");
        int middlePartsCount = msg.getMiddlePartsCount();
        switch(i) {
            default:
                break;
            case RPL_EXT_ISON:
                String[] nicksOnline = Acme.Utils.splitStr(msg.getTrailing().trim(), ' ');
                Lang.ASSERT_NOT_NULL(nicksOnline, "nicksOnline");
                il.handleIsonReply(msg.getPrefix(), nicksOnline);
                return;
            case RPL_EXT_NOTICE:
                try {
                    il.handleNotice(msg.getPrefix(), msg.getSender(), msg.getTrailing());
                } catch (Exception ex) {
                    il.handleNotice(msg.getPrefix(), null, msg.getTrailing());
                }
                return;
            case 002:
                String s002 = msg.getTrailing();
                break;
            case 401:
                il.handleNoSuchNickChannel(msg.getMiddlePart(1), msg.getTrailing());
                return;
            case 301:
                il.handleAwayMessage(msg.getMiddlePart(1), msg.getTrailing());
                return;
            case 405:
            case 471:
            case 473:
            case 474:
            case 475:
                il.cannotJoinChannel(msg.getMiddlePart(1), msg.getTrailing());
                return;
            case 432:
            case 433:
            case 436:
                il.invalidNickName(msg.getMiddlePart(1), msg.getTrailing());
                return;
            case 311:
                il.handleWhoisUser(msg.getMiddlePart(1), msg.getMiddlePart(2), msg.getMiddlePart(3), msg.getTrailing());
                return;
            case 312:
                il.handleWhoisServer(msg.getMiddlePart(1), msg.getMiddlePart(2), msg.getTrailing());
                return;
            case 313:
                il.handleWhoisOperator(msg.getMiddlePart(1), msg.getTrailing());
                return;
            case 317:
                il.handleWhoisIdleTime(msg.getMiddlePart(1), Integer.parseInt(msg.getMiddlePart(2)), msg.getTrailing());
                return;
            case 319:
                il.handleWhoisChannelsOn(msg.getMiddlePart(1), msg.getTrailing());
                return;
            case 318:
                il.handleWhoisEnd(msg.getMiddlePart(1), msg.getTrailing());
                return;
            case 331:
                il.onInitialTopic(msg.getMiddlePart(1), false, msg.getTrailing());
                return;
            case 332:
                il.onInitialTopic(msg.getMiddlePart(1), true, msg.getTrailing());
                return;
            case 367:
                il.handleBanListItem(msg.getMiddlePart(1), msg.getMiddlePart(2));
                return;
            case 368:
                il.handleBanListEnd(msg.getMiddlePart(1));
                return;
            case -101:
                {
                    String to = msg.getMiddlePart(0);
                    Lang.ASSERT_NOT_NULL_NOR_EMPTY(to, "privmsg.to");
                    User userFrom = msg.getSender();
                    Lang.ASSERT_NOT_NULL(userFrom, "userFrom");
                    if (msg.getTrailing() != null) {
                        String s = msg.getTrailing();
                        if (s.startsWith("\001")) {
                            boolean flag1 = s.endsWith("\001") && s.length() > 1;
                            int j = flag1 ? s.length() - 1 : s.length();
                            handleCtcp(msg, s.substring(1, j), il);
                            return;
                        }
                    }
                    il.textMessageReceived(userFrom, to, msg.getTrailing());
                    return;
                }
            case -100:
                il.ping(msg.getTrailing());
                return;
            case -103:
                handleJoin(il, msg);
                return;
            case 353:
                parseNamReply(il, msg);
                return;
            case RPL_EXT_PART:
                {
                    String channelName = msg.getMiddlePart(0);
                    IRCUser userFrom = msg.getSender();
                    Lang.ASSERT_NOT_NULL(userFrom, "userFrom");
                    IRCChannelParticipant IRCChannelRole = queryClient.getChannelRoleByChannelName(userFrom, channelName);
                    Lang.EXPECT_NOT_NULL(IRCChannelRole, "IRCChannelRole");
                    IRCChannel channel = (IRCChannel) IRCChannelRole.getRoom();
                    channel.parts(IRCChannelRole, msg.getTrailing());
                    il.handlePart(channelName, userFrom, msg.getTrailing() != null ? msg.getTrailing() : userFrom.getActiveNick());
                    return;
                }
            case -106:
                il.handleTopic(msg.getMiddlePart(0), msg.getTrailing());
                return;
            case -105:
                {
                    IRCUser userFrom = msg.getSender();
                    Lang.ASSERT_NOT_NULL(userFrom, "userFrom");
                    queryClient.onQuit(userFrom);
                    il.quit(userFrom.getActiveNick(), msg.getTrailing());
                    return;
                }
            case -104:
                {
                    IRCUser userFrom = msg.getSender();
                    Lang.ASSERT_NOT_NULL(userFrom, "userFrom");
                    String oldNickName = userFrom.getActiveNick();
                    String newNickName = msg.getTrailing();
                    if (newNickName == null) newNickName = msg.getMiddlePart(0);
                    il.nickChange(oldNickName, newNickName);
                    userFrom.setActiveNick(newNickName);
                    return;
                }
            case -108:
                {
                    IRCUser userFrom = msg.getSender();
                    Lang.ASSERT_NOT_NULL(userFrom, "userFrom");
                    String channelName = msg.getMiddlePart(0);
                    String kickedNick = msg.getMiddlePart(1);
                    IRCChannel IRCChannel = queryClient.getChannelJoinedByChannelName(channelName);
                    Lang.EXPECT_NOT_NULL(IRCChannel, "IRCChannel named " + StringUtil.toPrintableString(channelName));
                    IRCChannelParticipant IRCChannelRole = queryClient.getChannelRoleByNickName(IRCChannel, kickedNick);
                    Lang.EXPECT_NOT_NULL(IRCChannelRole, "IRCChannelRole for " + StringUtil.toPrintableString(kickedNick) + " on " + StringUtil.toPrintableString(channelName));
                    IRCChannel.kicked(IRCChannelRole, userFrom, msg.getTrailing());
                    il.kick(userFrom.getActiveNick(), channelName, kickedNick, msg.getTrailing());
                    return;
                }
            case 1:
                il.welcome(msg.getMiddlePart(0), msg.getTrailing());
                queryClient.welcome_setNickName(msg.getMiddlePart(0));
                return;
            case RPL_EXT_MODE:
            case 324:
                final boolean is324 = i == 324;
                Vector vector = msg.getMiddleParts();
                if (vector.size() >= 1) {
                    if (is324) {
                        vector.removeElementAt(0);
                    }
                    String channelName = null;
                    String modeChars = null;
                    if (vector.size() >= 1) {
                        channelName = msg.getMiddlePart(is324 ? 1 : 0);
                        vector.removeElementAt(0);
                        if (vector.size() >= 1) {
                            modeChars = msg.getMiddlePart(is324 ? 2 : 1);
                            vector.removeElementAt(0);
                        }
                    }
                    il.handleModeChangeRaw(msg.getPrefix(), channelName != null ? channelName : msg.getTrailing(), modeChars != null ? modeChars : msg.getTrailing(), vector);
                }
                if (!is324) parseModeChange(msg, il);
                return;
            case 321:
                il.handleListStart();
                return;
            case 322:
                il.handleListItem(msg.getMiddlePart(1), Integer.parseInt(msg.getMiddlePart(2)), msg.getTrailing());
                return;
            case 323:
                il.handleListEnd();
                return;
        }
        il.unhandledCommand(msg);
    }

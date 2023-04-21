    public void messageReceived(IRCMessageEvent e) {
        if (e.isConsumed()) return;
        IRCMessage msg = e.getMessage();
        if (msg.getType().equals(IRCMessageTypes.MSG_ADMIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_AWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_CONNECT)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_DIE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_ERROR)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_INFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_INVITE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_ISON)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_JOIN)) {
            ArrayList chans = JoinMessage.getChannels(msg);
            for (int i = 0; i < chans.size(); i++) forwardMessage(e, (String) chans.get(i));
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_KICK)) {
            forwardMessage(e, KickMessage.getChannel(msg));
            if (KickMessage.getUser(msg).equals(user.getNick())) removeChannel(KickMessage.getChannel(msg));
        } else if (msg.getType().equals(IRCMessageTypes.MSG_KILL)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LUSERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_MODE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_MOTD)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NAMES)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NICK)) {
            if (msg.getNick().equals(user.getNick())) user.setNick(NickMessage.getNickname(msg));
            forwardMessage(e, null);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NOTICE)) {
            MessageFormat f = new MessageFormat("[{0}] {1}");
            try {
                Object[] o = f.parse(NoticeMessage.getText(msg));
                if (IRCUtils.isChannel(o[0].toString())) {
                    forwardMessage(e, o[0].toString());
                    e.consume();
                    fireMessageProcessedEvent(msg);
                    return;
                }
            } catch (ParseException exc) {
            }
        } else if (msg.getType().equals(IRCMessageTypes.MSG_OPER)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PART)) {
            ArrayList chans = PartMessage.getChannels(msg);
            for (int i = 0; i < chans.size(); i++) {
                forwardMessage(e, (String) chans.get(i));
                if (msg.getNick().equals(user.getNick())) removeChannel((String) chans.get(i));
            }
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PASS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PING)) {
            conn.send(PongMessage.createMessage("", "", "", PingMessage.getServer1(msg)));
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PONG)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PRIVMSG)) {
            if (IRCUtils.isChannel(PrivateMessage.getMsgtarget(msg))) forwardMessage(e, PrivateMessage.getMsgtarget(msg)); else {
                if (CTCPMessage.isCTCPMessage(PrivateMessage.getText(msg))) return; else forwardMessage(e, msg.getNick());
            }
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_QUIT)) {
            forwardMessage(e, null);
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_REHASH)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_RESTART)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SERVICE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SERVLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SQUERY)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SQUIT)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_STATS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_SUMMON)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_TIME)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_TOPIC)) {
            forwardMessage(e, TopicMessage.getChannel(msg));
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_TRACE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USER)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USERHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_VERSION)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WALLOPS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHO)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHOIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHOWAS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHNICK)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHSERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHCHANNEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CANNOTSENDTOCHAN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_TOOMANYCHANNELS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_WASNOSUCHNICK)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_TOOMANYTARGETS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHSERVICE)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOORIGIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NORECIPIENT)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTEXTTOSEND)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTOPLEVEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_WILDTOPLEVEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADMASK)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNKNOWNCOMMAND)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOMOTD)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOADMININFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_FILEERROR)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NONICKNAMEGIVEN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_ERRONEUSNICKNAME)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NICKNAMEINUSE)) {
            if (isNickChange() == false) {
                disconnect();
                e.consume();
                fireMessageProcessedEvent(msg);
                reconnect = true;
            }
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NICKCOLLISION)) {
            disconnect();
            e.consume();
            fireMessageProcessedEvent(msg);
            reconnect = true;
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNAVAILRESOURCE)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERNOTINCHANNEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTONCHANNEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERONCHANNEL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOLOGIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_SUMMONDISABLED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERSDISABLED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTREGISTERED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NEEDMOREPARAMS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_ALREADYREGISTERED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOPERMFORHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_PASSWDMISMATCH)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_YOUREBANNEDCREEP)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_YOUWILLBEBANNED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_KEYSET)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CHANNELISFULL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNKNOWNMODE)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_INVITEONLYCHAN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BANNEDFROMCHAN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADCHANNELKEY)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADCHANMASK)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOCHANMODES)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BANLISTFULL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOPRIVILEGES)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CHANOPRIVSNEEDED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CANTKILLSERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_RESTRICTED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNIQOPPRIVSNEEDED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOOPERHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UMODEUNKNOWNFLAG)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERSDONTMATCH)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WELCOME)) {
            String str = WelcomeReply.getNick(msg);
            if (user.getNick().startsWith(str)) user.setNick(str);
            if (conn.isActive()) conn.setStatus(Session.CONNECTED);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_YOURHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_CREATED)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MYINFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_BOUNCE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACELINK)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACECONNECTING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEHANDSHAKE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEUNKNOWN)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEOPERATOR)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEUSER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACESERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACESERVICE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACENEWTYPE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSLINKINFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSCOMMANDS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSCLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSNLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSILINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSKLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSYLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFSTATS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_UMODEIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_SERVLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSLLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSUPTIME)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSOLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_STATSHLINE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSERCLIENT)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSEROP)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSERUNKNOWN)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSERCHANNELS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LUSERME)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ADMINME)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ADMINLOC1)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ADMINLOC2)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ADMINEMAIL)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACELOG)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEEND)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRYAGAIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NONE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_AWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_USERHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ISON)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_UNAWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NOWAWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISUSER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISSERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISOPERATOR)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOWASUSER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFWHO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISIDLE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFWHOIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOISCHANNELS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LISTSTART)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LISTEND)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_CHANNELMODEIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_UNIQOPIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NOTOPIC)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TOPIC)) {
            forwardMessage(e, TopicReply.getChannel(msg));
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INVITING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_SUMMONING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INVITELIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFINVITELIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFEXCEPTLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_VERSION)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOREPLY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NAMREPLY)) {
            forwardMessage(e, NamesReply.getChannel(msg));
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFLINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFNAMES)) {
            forwardMessage(e, EndofnamesReply.getChannel(msg));
            e.consume();
            fireMessageProcessedEvent(msg);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_BANLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFBANLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFWHOWAS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MOTD)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFINFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MOTDSTART)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFMOTD)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_YOUREOPER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_REHASHING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_YOURESERVICE)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TIME)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_USERSSTART)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_USERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFUSERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NOUSERS)) {
        }
    }

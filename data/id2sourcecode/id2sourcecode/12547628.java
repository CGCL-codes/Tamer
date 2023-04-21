    public void messageProcessed(AbstractIRCSession session, IRCMessage msg) {
        if (msg.getType().equals(IRCMessageTypes.MSG_ADMIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_AWAY)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_CONNECT)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_DIE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_ERROR)) {
            String args[] = new String[] { net.hypotenubel.irc.msgutils.ErrorMessage.getErrorMessage(msg) };
            String txt = App.localization.localize("app", "irc.msg_error", "An error has occurred: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_INFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_INVITE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_ISON)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_JOIN)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_KICK)) {
            if (!KickMessage.getUser(msg).equals(session.getNickName())) return;
            ArrayList channels = KickMessage.getChannels(msg);
            for (int i = 0; i < channels.size(); i++) {
                String comment = KickMessage.getComment(msg);
                String txt;
                if (comment.equals("") || comment.equals(msg.getNick())) {
                    String args[] = new String[] { (String) channels.get(i), msg.getNick() };
                    txt = App.localization.localize("app", "irc.msg_kick.user.nocomment", "You have been kicked out of the channel {0} by {1}", args);
                } else {
                    String args[] = new String[] { (String) channels.get(i), msg.getNick(), comment };
                    txt = App.localization.localize("app", "irc.msg_kick.user.comment", "You have been kicked out of the channel {0} by {1} " + "({2})", args);
                }
                append(txt, "KICK", false);
            }
        } else if (msg.getType().equals(IRCMessageTypes.MSG_KILL)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_LUSERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_MODE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_MOTD)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NAMES)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NICK)) {
            if (NickMessage.getNickname(msg).equals(session.getNickName())) {
                String args[] = new String[] { NickMessage.getNickname(msg) };
                String txt = App.localization.localize("app", "irc.msg_nick.self", "You changed your nick to {0}", args);
                append(txt, "NICK", false);
            }
        } else if (msg.getType().equals(IRCMessageTypes.MSG_NOTICE)) {
            MessageFormat f = new MessageFormat("[{0}] {1}");
            try {
                Object[] o = f.parse(NoticeMessage.getText(msg));
                if (IRCUtils.isChannel(o[0].toString())) return;
            } catch (ParseException exc) {
            }
            String origin = msg.getNick();
            String txt;
            if (origin.equals("")) {
                String args[] = new String[] { NoticeMessage.getText(msg) };
                txt = App.localization.localize("app", "irc.msg_notice.noorigin", "{0}", args);
            } else {
                String args[] = new String[] { origin, NoticeMessage.getText(msg) };
                txt = App.localization.localize("app", "irc.msg_notice", "{0}: {1}", args);
            }
            append(txt, "NOTICE", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_OPER)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PART)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PASS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PING)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PONG)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_PRIVMSG)) {
            if (!CTCPMessage.isCTCPMessage(PrivateMessage.getText(msg))) return;
            CTCPMessage ctcp = CTCPMessage.parseMessageString(PrivateMessage.getText(msg));
            if (ctcp.getType().equals(CTCPMessageTypes.CTCP_CLIENTINFO)) {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp.clientinfo", "{0} just requested client information (CTCP Clientinfo)", args);
                append(txt, "CTCP", false);
            } else if (ctcp.getType().equals(CTCPMessageTypes.CTCP_PING)) {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp.ping", "{0} just pinged you (CTCP Ping)", args);
                append(txt, "CTCP", false);
            } else if (ctcp.getType().equals(CTCPMessageTypes.CTCP_TIME)) {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp.time", "{0} just timed you (CTCP Time)", args);
                append(txt, "CTCP", false);
            } else if (ctcp.getType().equals(CTCPMessageTypes.CTCP_VERSION)) {
                String args[] = new String[] { msg.getNick() };
                String txt = App.localization.localize("app", "irc.msg_privmsg.ctcp.version", "{0} just versioned you (CTCP Version)", args);
                append(txt, "CTCP", false);
            }
        } else if (msg.getType().equals(IRCMessageTypes.MSG_QUIT)) {
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
        } else if (msg.getType().equals(IRCMessageTypes.MSG_TRACE)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USER)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USERHOST)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_USERS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_VERSION)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WALLOPS)) {
            String origin = msg.getNick();
            String txt;
            if (origin.equals("")) {
                String args[] = new String[] { WallopsMessage.getText(msg) };
                txt = App.localization.localize("app", "irc.msg_wallops.noorigin", "WALLOPS: {0}", args);
            } else {
                String args[] = new String[] { origin, WallopsMessage.getText(msg) };
                txt = App.localization.localize("app", "irc.msg_wallops", "WALLOPS from {0}: {1}", args);
            }
            append(txt, "WALLOPS", false);
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHO)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHOIS)) {
        } else if (msg.getType().equals(IRCMessageTypes.MSG_WHOWAS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHNICK)) {
            String args[] = new String[] { NosuchnickError.getNickname(msg) };
            String txt = App.localization.localize("app", "irc.err_nosuchnick", "No such nick: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHSERVER)) {
            String args[] = new String[] { NosuchserverError.getServername(msg) };
            String txt = App.localization.localize("app", "irc.err_nosuchserver", "No such server: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHCHANNEL)) {
            String args[] = new String[] { NosuchchannelError.getChannelname(msg) };
            String txt = App.localization.localize("app", "irc.err_nosuchchannel", "No such channel: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CANNOTSENDTOCHAN)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_TOOMANYCHANNELS)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_WASNOSUCHNICK)) {
            String args[] = new String[] { WasnosuchnickError.getNickname(msg) };
            String txt = App.localization.localize("app", "irc.err_wasnosuchnick", "There was no such nick: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_TOOMANYTARGETS)) {
            String txt = App.localization.localize("app", "irc.err_toomanytargets", "Too many targets");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOSUCHSERVICE)) {
            String args[] = new String[] { NosuchserviceError.getServicename(msg) };
            String txt = App.localization.localize("app", "irc.err_nosuchservice", "No such service: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOORIGIN)) {
            String txt = App.localization.localize("app", "irc.err_noorigin", "Ping or Pong failed: No origin specified");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NORECIPIENT)) {
            String txt = App.localization.localize("app", "irc.err_norecipient", "Command failed: No recipient specified");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTEXTTOSEND)) {
            String txt = App.localization.localize("app", "irc.err_notexttosend", "No text to send");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTOPLEVEL)) {
            String args[] = new String[] { NotoplevelError.getMask(msg) };
            String txt = App.localization.localize("app", "irc.err_notoplevel", "No top level domain specified: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_WILDTOPLEVEL)) {
            String args[] = new String[] { WildtoplevelError.getMask(msg) };
            String txt = App.localization.localize("app", "irc.err_wildtoplevel", "Wildcard in toplevel domain: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADMASK)) {
            String args[] = new String[] { BadmaskError.getMask(msg) };
            String txt = App.localization.localize("app", "irc.err_badmask", "Bad server / host mask: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNKNOWNCOMMAND)) {
            String args[] = new String[] { UnknowncommandError.getCommand(msg) };
            String txt = App.localization.localize("app", "irc.err_unknowncommand", "Unknown command: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOMOTD)) {
            String txt = App.localization.localize("app", "irc.err_nomotd", "No message of the day available");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOADMININFO)) {
            String txt = App.localization.localize("app", "irc.err_noadmininfo", "No administrative information available");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_FILEERROR)) {
            String txt = App.localization.localize("app", "irc.err_fileerror", "A file error occurred");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NONICKNAMEGIVEN)) {
            String txt = App.localization.localize("app", "irc.err_nonicknamegiven", "No nick specified");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_ERRONEUSNICKNAME)) {
            String args[] = new String[] { ErroneusnicknameError.getNick(msg) };
            String txt = App.localization.localize("app", "irc.err_erroneusnickname", "Invalid nick: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NICKNAMEINUSE)) {
            String args[] = new String[] { NicknameinuseError.getNick(msg) };
            String txt = App.localization.localize("app", "irc.err_nicknameinuse", "This nick is already in use: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NICKCOLLISION)) {
            String args[] = new String[] { NickcollisionError.getNick(msg) };
            String txt = App.localization.localize("app", "irc.err_nickcollision", "This nick was already in use: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNAVAILRESOURCE)) {
            String args[] = new String[] { UnavailresourceError.getNick(msg) };
            String txt = App.localization.localize("app", "irc.err_unavailresource", "Nick / channel name is currently blocked: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERNOTINCHANNEL)) {
            String args[] = new String[] { UsernotinchannelError.getNick(msg), UsernotinchannelError.getChannel(msg) };
            String txt = App.localization.localize("app", "irc.err_usernotinchannel", "{0} is not on the channel: {1}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTONCHANNEL)) {
            String args[] = new String[] { NotonchannelError.getChannel(msg) };
            String txt = App.localization.localize("app", "irc.err_notonchannel", "You are not part of that channel: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERONCHANNEL)) {
            String args[] = new String[] { UseronchannelError.getNick(msg), UseronchannelError.getChannel(msg) };
            String txt = App.localization.localize("app", "irc.err_useronchannel", "{0} is already on the channel: {1}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOLOGIN)) {
            String args[] = new String[] { NologinError.getUser(msg) };
            String txt = App.localization.localize("app", "irc.err_nologin", "{0} is not logged in", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_SUMMONDISABLED)) {
            String txt = App.localization.localize("app", "irc.err_summondisabled", "The SUMMON command is not available on this server");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERSDISABLED)) {
            String txt = App.localization.localize("app", "irc.err_usersdisabled", "The USERS command is not available on this server");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOTREGISTERED)) {
            String txt = App.localization.localize("app", "irc.err_notregistered", "You have not registered");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NEEDMOREPARAMS)) {
            String args[] = new String[] { NeedmoreparamsError.getCommand(msg) };
            String txt = App.localization.localize("app", "irc.err_needmoreparams", "Not enough parameters: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_ALREADYREGISTERED)) {
            String txt = App.localization.localize("app", "irc.err_alreadyregistered", "You are already registered");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOPERMFORHOST)) {
            String txt = App.localization.localize("app", "irc.err_nopermforhost", "You have no permission to connect to this server");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_PASSWDMISMATCH)) {
            String txt = App.localization.localize("app", "irc.err_passwdmismatch", "The password is incorrect");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_YOUREBANNEDCREEP)) {
            String txt = App.localization.localize("app", "irc.err_yourebannedcreep", "You are banned from this server");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_YOUWILLBEBANNED)) {
            String txt = App.localization.localize("app", "irc.err_youwillbebanned", "You will be banned from this server if you don't change " + "your behaviour");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_KEYSET)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CHANNELISFULL)) {
            String args[] = new String[] { ChannelisfullError.getChannel(msg) };
            String txt = App.localization.localize("app", "irc.err_channelisfull", "The channel is already full: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNKNOWNMODE)) {
            String args[] = new String[] { UnknownmodeError.getCharacter(msg) };
            String txt = App.localization.localize("app", "irc.err_unknownmode", "Unknown mode: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_INVITEONLYCHAN)) {
            String args[] = new String[] { InviteonlychanError.getChannel(msg) };
            String txt = App.localization.localize("app", "irc.err_inviteonlychan", "You can't join this channel unless you're invited: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BANNEDFROMCHAN)) {
            String args[] = new String[] { BannedfromchanError.getChannel(msg) };
            String txt = App.localization.localize("app", "irc.err_bannedfromchan", "You're banned from the channel: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADCHANNELKEY)) {
            String args[] = new String[] { BadchannelkeyError.getChannel(msg) };
            String txt = App.localization.localize("app", "irc.err_badchannelkey", "Can't join channel because of an incorrect channel key: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BADCHANMASK)) {
            String args[] = new String[] { BadchanmaskError.getChannel(msg) };
            String txt = App.localization.localize("app", "irc.err_badchanmask", "Bad channel mask: {0}", args);
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOCHANMODES)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_BANLISTFULL)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOPRIVILEGES)) {
            String txt = App.localization.localize("app", "irc.err_noprivileges", "You lack the required privileges");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CHANOPRIVSNEEDED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_CANTKILLSERVER)) {
            String txt = App.localization.localize("app", "irc.err_cantkillserver", "You can't kill a server");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_RESTRICTED)) {
            String txt = App.localization.localize("app", "irc.err_restricted", "Your connection is restricted");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UNIQOPPRIVSNEEDED)) {
        } else if (msg.getType().equals(IRCMessageTypes.ERR_NOOPERHOST)) {
            String txt = App.localization.localize("app", "irc.err_nooperhost", "You can't acquire operator status");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_UMODEUNKNOWNFLAG)) {
            String txt = App.localization.localize("app", "irc.err_umodeunknown", "Unknown user mode");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.ERR_USERSDONTMATCH)) {
            String txt = App.localization.localize("app", "irc.err_usersdontmatch", "You can't change other people's mode");
            append(txt, "ERROR", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WELCOME)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_YOURHOST)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_CREATED)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MYINFO)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_BOUNCE)) {
            String server = BounceReply.getServername(msg);
            String port = BounceReply.getPortnumber(msg);
            if (server.equals("") || port.equals("")) {
                append(msg.getArgs().get(msg.getArgs().size() - 1), "REPLY", false);
            } else {
                String args[] = new String[] { server, port };
                String txt = App.localization.localize("app", "irc.rpl_bounce", "Try the following server: {0}, port {1}", args);
                append(txt, "REPLY", false);
            }
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACELINK)) {
            String args[] = new String[] { msg.getUserDescriptor().toString(), TracelinkReply.getVersion(msg), TracelinkReply.getDestination(msg), TracelinkReply.getNext(msg), TracelinkReply.getProtocol(msg), TracelinkReply.getLinkuptime(msg), TracelinkReply.getBackstream(msg), TracelinkReply.getUpstream(msg) };
            String txt = App.localization.localize("app", "irc.rpl_tracelink", "Trace link: source {0}, server version {1}, " + "protocol version {4}, uptime {5}, backstream {6}, " + "upstream {7}", args);
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACECONNECTING)) {
            String args[] = new String[] { msg.getUserDescriptor().toString(), TraceconnectingReply.getTrcclass(msg), TraceconnectingReply.getServer(msg) };
            String txt = App.localization.localize("app", "irc.rpl_traceconnecting", "Trace connecting: source {0}, class {1}, server {2}", args);
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEHANDSHAKE)) {
            String args[] = new String[] { msg.getUserDescriptor().toString(), TracehandshakeReply.getTrcclass(msg), TracehandshakeReply.getServer(msg) };
            String txt = App.localization.localize("app", "irc.rpl_tracehandshake", "Trace connecting: source {0}, class {1}, server {2}", args);
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEUNKNOWN)) {
            String args[] = new String[] { msg.getUserDescriptor().toString(), TraceunknownReply.getTrcclass(msg), TraceunknownReply.getIP(msg) };
            String txt = App.localization.localize("app", "irc.rpl_traceunknown", "Trace unknown: source {0}, class {1}, IP {2}", args);
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEOPERATOR)) {
            String args[] = new String[] { msg.getUserDescriptor().toString(), TraceoperatorReply.getTrcclass(msg), TraceoperatorReply.getNick(msg) };
            String txt = App.localization.localize("app", "irc.rpl_traceoperator", "Trace operator: source {0}, class {1}, nick {2}", args);
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACEUSER)) {
            String args[] = new String[] { msg.getUserDescriptor().toString(), TraceuserReply.getTrcclass(msg), TraceuserReply.getNick(msg) };
            String txt = App.localization.localize("app", "irc.rpl_traceuser", "Trace user: source {0}, class {1}, nick {2}", args);
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACESERVER)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACESERVICE)) {
            String args[] = new String[] { msg.getUserDescriptor().toString(), TraceserviceReply.getTrcclass(msg), TraceserviceReply.getName(msg), TraceserviceReply.getType(msg), TraceserviceReply.getActive(msg) };
            String txt = App.localization.localize("app", "irc.rpl_traceservice", "Trace operator: source {0}, class {1}, name {2}, type {3}, " + "active type {4}", args);
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TRACENEWTYPE)) {
            String args[] = new String[] { msg.getUserDescriptor().toString(), TracenewtypeReply.getNewtype(msg), TracenewtypeReply.getName(msg) };
            String txt = App.localization.localize("app", "irc.rpl_tracenewtype", "Trace user: source {0}, type {1}, name {2}", args);
            append(txt, "REPLY", false);
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
        } else if (msg.getType().equals(IRCMessageTypes.RPL_TOPICSET)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INVITING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_SUMMONING)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INVITELIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFINVITELIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFEXCEPTLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_VERSION)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_WHOREPLY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_NAMREPLY)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_LINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFLINKS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFNAMES)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_BANLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFBANLIST)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFWHOWAS)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_INFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MOTD)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFINFO)) {
        } else if (msg.getType().equals(IRCMessageTypes.RPL_MOTDSTART)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
        } else if (msg.getType().equals(IRCMessageTypes.RPL_ENDOFMOTD)) {
            String txt = msg.getArgs().get(msg.getArgs().size() - 1).toString();
            append(txt, "REPLY", false);
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

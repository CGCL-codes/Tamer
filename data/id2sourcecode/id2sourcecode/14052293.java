    void parseEvent(IRCEvent event) {
        Session session = event.getSession();
        System.out.println("PARSEEVENT1: " + session);
        System.out.println("PARSEEVENT2: " + session.getConnection());
        Connection con = session.getConnection();
        String data = event.getRawEventData();
        String nick = con.getProfile().getActualNick();
        String[] tokens = data.split("\\s+");
        if (tokens.length > 1) {
            if (tokens[1].matches("^\\d{3}$")) {
                numericEvent(data, con, event, Integer.parseInt(tokens[1]));
            } else {
                String command = tokens[1];
                if (command.equals("PRIVMSG")) {
                    message(data, con);
                } else if (command.equals("QUIT")) {
                    QuitEvent qEvent = IRCEventFactory.quit(data, con);
                    con.removeNickFromAllChannels(qEvent.getWho());
                    manager.addToRelayList(qEvent);
                } else if (command.equals("JOIN")) {
                    Pattern p = Pattern.compile("^:\\Q" + nick + "\\E\\!.*?\\s+JOIN\\s+:?(\\S+)$");
                    Matcher m = p.matcher(data);
                    if (m.matches()) {
                        Channel channel = new Channel(m.group(1).toLowerCase(), manager.getSessionFor(con));
                        con.addChannel(channel);
                        manager.getSessionFor(con).addChannelName(channel.getName());
                        manager.addToRelayList(IRCEventFactory.joinCompleted(data, con, nick, channel));
                    } else {
                        JoinEvent jEvent = IRCEventFactory.regularJoin(data, con);
                        jEvent.getChannel().addNick(jEvent.getNick());
                        manager.addToRelayList(jEvent);
                    }
                } else if (command.equals("MODE")) {
                    mode(event);
                } else if (command.equals("PART")) {
                    PartEvent pEvent = IRCEventFactory.part(data, con);
                    if (!pEvent.getChannel().removeNick(pEvent.getWho())) {
                        System.err.println("Could Not remove nick " + pEvent.getWho() + " from " + pEvent.getChannelName());
                    }
                    if (pEvent.getWho().equalsIgnoreCase(nick)) {
                        con.removeChannel(pEvent.getChannel());
                        manager.getSessionFor(con).removeChannelName(pEvent.getChannelName());
                    }
                    manager.addToRelayList(pEvent);
                } else if (command.equals("NOTICE")) {
                    manager.addToRelayList(IRCEventFactory.notice(data, con));
                } else if (command.equals("TOPIC")) {
                    Pattern p = Pattern.compile("^.+?TOPIC\\s+(.+?)\\s+.*$");
                    Matcher m = p.matcher(data);
                    m.matches();
                    event.getSession().sayRaw("TOPIC " + m.group(1));
                } else if (command.equals("INVITE")) {
                    manager.addToRelayList(IRCEventFactory.invite(data, con));
                } else if (command.equals("NICK")) {
                    NickChangeEvent nEvent = IRCEventFactory.nickChange(data, con);
                    con.nickChanged(nEvent.getOldNick(), nEvent.getNewNick());
                    if (nEvent.getOldNick().equals(nick)) {
                        event.getSession().updateProfileSuccessfully(true);
                    }
                    manager.addToRelayList(nEvent);
                } else if (command.equals("KICK")) {
                    KickEvent ke = IRCEventFactory.kick(data, con);
                    if (!ke.getChannel().removeNick(ke.getWho())) {
                        log.info("COULD NOT REMOVE NICK " + ke.getWho() + " from channel " + ke.getChannel().getName());
                    }
                    if (ke.getWho().equals(nick)) {
                        con.removeChannel(ke.getChannel());
                        if (manager.getSessionFor(con).isRejoinOnKick()) manager.getSessionFor(con).join(ke.getChannel().getName());
                    }
                    manager.addToRelayList(ke);
                } else if (data.matches("^PING.*")) {
                    con.pong(event);
                    manager.addToRelayList(event);
                } else if (data.matches(".*PONG.*")) {
                    con.gotPong();
                    manager.addToRelayList(event);
                } else if (data.matches("^NOTICE\\s+(.*$)$")) {
                    manager.addToRelayList(IRCEventFactory.notice(data, con));
                } else {
                    manager.addToRelayList(event);
                }
            }
        }
    }

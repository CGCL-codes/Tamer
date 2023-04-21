            @Override
            public boolean handle(RawText message) {
                log(LogLevel.Out, message.getLog());
                if (client != null) {
                    Integer marketChannelIndex = chatting.getOpenChannelIndex(Channel.Market);
                    Integer guildChannelIndex = chatting.getOpenChannelIndex(Channel.Guild);
                    int channelId = 0;
                    int marketChannelId = getChannelId("market");
                    int guildChannelId = getChannelId("guild");
                    int gmChannelId = getChannelId("#gm");
                    switch(message.getChannel()) {
                        case Protocol.CHAT_CHANNEL1:
                            if (marketChannelIndex != null && marketChannelIndex == 0) channelId = marketChannelId;
                            if (guildChannelIndex != null && guildChannelIndex == 0) channelId = guildChannelId;
                            break;
                        case Protocol.CHAT_CHANNEL2:
                            if (marketChannelIndex != null && marketChannelIndex == 1) channelId = marketChannelId;
                            if (guildChannelIndex != null && guildChannelIndex == 1) channelId = guildChannelId;
                            break;
                        case Protocol.CHAT_CHANNEL3:
                            if (marketChannelIndex != null && marketChannelIndex == 2) channelId = marketChannelId;
                            if (guildChannelIndex != null && guildChannelIndex == 2) channelId = guildChannelId;
                            break;
                        case Protocol.CHAT_GM:
                            channelId = gmChannelId;
                            break;
                    }
                    for (GuildMember m : data.getGuild().members) {
                        if (m.isListenToChannel(channelId)) client.sendMessage(m.jid, message.getContent());
                    }
                }
                String s = null;
                if (message.isPresonalChat()) {
                    s = Parser.getPlayerNameFromPrivateMessage(message.getMessage());
                    if (s != null) {
                        String cnt = Parser.getContentOfPrivateMessage(message.getMessage());
                        respond(s, cnt);
                    }
                    return false;
                }
                if (message.isServerChat()) {
                    s = Parser.getPlayerNameFromTradeMessage(message.getMessage());
                    if (s != null) {
                        if (data.getPrivileges().isBanned(s)) {
                            appendAnserw(s, jelb.Locale.MSG_BAN_INFO);
                            send(new Message(Protocol.REJECT_TRADE));
                            sendExitTrade();
                        } else {
                            log(LogLevel.Debug, "Trade request from:" + s);
                            if (data.getConfig().autoSit) send(new Message(Protocol.SIT_DOWN, new Uint8(0)));
                            somebodyWaitToTrade = s;
                        }
                        return false;
                    }
                    if (message.getContent().startsWith("You are in", 0)) {
                        lastLocationOnMap = new MapLocation(message.getContent());
                        if (locator != null) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("Im located in ");
                            sb.append(lastLocationOnMap.getMapName());
                            sb.append(" at ");
                            sb.append(lastLocationOnMap.getLocation());
                            sendResponseByPm(locator, sb);
                            locator = null;
                        }
                        return false;
                    }
                    if (message.getContent().startsWith("Just an ordinary day") || message.getContent().startsWith("Today is a special day:")) {
                        send(new Message(Protocol.SIT_DOWN, new Uint8(0)));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                        }
                        if (message.getContent().startsWith("Just an ordinary day")) {
                            sendEmote(Emote.Stretch);
                        } else {
                            sendEmote(Emote.Jump);
                        }
                        dayDesc = message.getContent();
                        if (client != null) {
                            client.sendPresence(dayDesc.length() > 50 ? dayDesc.substring(0, 47) + "..." : dayDesc, true);
                            for (GuildMember m : data.getGuild().members) if (m.isListenToChannel(getChannelId("#day"))) client.sendMessage(m.jid, dayDesc);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                        }
                        send(new Message(Protocol.SIT_DOWN, new Uint8(1)));
                    }
                    if (message.getContent().startsWith("Game Date:")) day = message.getContent();
                }
                return false;
            }

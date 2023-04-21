    public TradeBot(String homePath) {
        super(homePath);
        this.data = new TradeBotData(this, new DataManager(this, homePath));
        this.loadConfig();
        this.players = new PlayerList(this);
        this.inventory = new Inventory();
        String wwwRoot = homePath == null ? "www/" : homePath + "/www/";
        this.wwwServer = new MicroWWWServer(wwwRoot, this, this.data.getConfig().getWwwServerPort());
        this.wwwServer.start();
        this.infoFileGenerator = new InfoFileGenerator(this);
        this.itemsKeeping = new ItemsKeeping(this);
        this.itemsKeeping.ItemInitialized.addListener(new ItemInitalizeRethrow());
        this.mobsObserving = new MobsObserving(this);
        this.chatting = new Chatting(this);
        this.addListener(new IMessageListener<LogInOk>() {

            @Override
            public LogInOk getMessageInstance() {
                return new LogInOk();
            }

            @Override
            public boolean handle(LogInOk message) {
                send(new Message(Protocol.LOCATE_ME));
                return false;
            }
        });
        this.addListener(new IMessageListener<RawText>() {

            @Override
            public RawText getMessageInstance() {
                return new RawText();
            }

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
        });
        this.addListener(new IMessageListener<NewMinute>() {

            @Override
            public NewMinute getMessageInstance() {
                return new NewMinute();
            }

            @Override
            public boolean handle(NewMinute message) {
                gameTimeHour = message.getHour();
                gameTimeMinute = message.getMinute();
                if (getGameDate() == "Uninitialized") {
                    send(new Message(Protocol.GET_DATE));
                }
                if (gameTimeHour == 6 && gameTimeMinute == 0) {
                    dayDesc = null;
                    send(new Message(Protocol.GET_DATE));
                    send(new Message(Protocol.RAW_TEXT, "#day"));
                }
                if (client != null && gameTimeHour == 5 && (gameTimeMinute > 54 || gameTimeMinute == 45 || gameTimeMinute == 30)) {
                    for (GuildMember m : data.getGuild().members) {
                        if (m.isListenToChannel(getChannelId("#day"))) {
                            client.sendMessage(m.jid, "New day will start in " + (60 - gameTimeMinute) + " minutes");
                        }
                    }
                }
                if (gameTimeHour == 5 && gameTimeMinute == 59 && data.getConfig().boardcastDayEnd) {
                    send(new Message(Protocol.SIT_DOWN, new Uint8(0)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                    sendGuildMessage("New day will start in 1 minute.");
                    sendEmote(Emote.CheerRight);
                }
                return false;
            }
        });
        this.addListener(new IMessageListener<HereYourStats>() {

            @Override
            public HereYourStats getMessageInstance() {
                return new HereYourStats();
            }

            @Override
            public boolean handle(HereYourStats message) {
                load = message.getLoad();
                return false;
            }
        });
        this.addListener(new IMessageListener<HereYourInventory>() {

            @Override
            public HereYourInventory getMessageInstance() {
                return new HereYourInventory();
            }

            @Override
            public boolean handle(HereYourInventory message) {
                if (lastSuccessfullyTrade != null) {
                    Message commitMsg = lastSuccessfullyTrade.commit();
                    if (commitMsg != null) send(commitMsg);
                    lastSuccessfullyTrade = null;
                }
                return false;
            }
        });
        this.addListener(new IMessageListener<GetTradeObject>(this) {

            @Override
            public GetTradeObject getMessageInstance() {
                return new GetTradeObject();
            }

            @Override
            public boolean handle(GetTradeObject message) {
                reactionOnBalancedTradeWasSent = false;
                scratchHeadOnBalancingTradeWasSent = false;
                shakeHeadOnBalancingTradeWasSent = false;
                if (message.getWho().toByte() == Protocol.YOU) {
                    if (tradeEvent == null && tradePartner != null) {
                        appendAnserw(tradePartner.getName(), "Hi lets do buisness");
                        tradeEvent = new BuyEvent((TradeBot) this.args, tradePartner);
                    }
                }
                if (tradeEvent == null) log(LogLevel.Debug, "Getting trade object with null trade event");
                return false;
            }
        });
        this.addListener(new IMessageListener<RemoveTradeObject>() {

            @Override
            public RemoveTradeObject getMessageInstance() {
                return new RemoveTradeObject();
            }

            @Override
            public boolean handle(RemoveTradeObject message) {
                reactionOnBalancedTradeWasSent = false;
                scratchHeadOnBalancingTradeWasSent = false;
                shakeHeadOnBalancingTradeWasSent = false;
                if (tradeEvent == null) log(LogLevel.Debug, "Getting remove trade object with null trade event");
                return false;
            }
        });
        this.addListener(new IMessageListener<GetTradeReject>() {

            @Override
            public GetTradeReject getMessageInstance() {
                return new GetTradeReject();
            }

            @Override
            public boolean handle(GetTradeReject message) {
                if (tradeEvent != null) {
                    if (message.getWho().toByte() == Protocol.YOU) {
                        tradeEvent.setYouAccept(0);
                    } else tradeEvent.setMeAccept(0);
                }
                return false;
            }
        });
        this.addListener(new IMessageListener<GetTradeExit>() {

            @Override
            public GetTradeExit getMessageInstance() {
                return new GetTradeExit();
            }

            @Override
            public boolean handle(GetTradeExit message) {
                log(LogLevel.Debug, "<trade exit");
                reactionOnBalancedTradeWasSent = false;
                if (tradeEvent != null) {
                    if (tradeEvent.isAcceptedAndConfirmed()) {
                        lastSuccessfullyTrade = tradeEvent;
                    } else {
                        sendEmote(Emote.Shrug);
                        tradeEvent = null;
                    }
                }
                if (data.getConfig().autoSit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                    }
                    send(new Message(Protocol.SIT_DOWN, new Uint8(1)));
                }
                tradeEvent = null;
                tradePartner = null;
                tickerTradeCancel.setSuspended(true);
                return false;
            }
        });
        this.addListener(new IMessageListener<GetTradeAccept>() {

            @Override
            public GetTradeAccept getMessageInstance() {
                return new GetTradeAccept();
            }

            @Override
            public boolean handle(GetTradeAccept message) {
                log(LogLevel.Debug, "<trade acccept:");
                if (tradeEvent == null) log(LogLevel.Error, "Getting trade accept with null trade event"); else {
                    if (message.getWho().toByte() == Protocol.YOU) {
                        tradeEvent.setYouAccept(tradeEvent.getYoursAcceptactionsCount() + 1);
                        jelb.trade.State tradeState = tradeEvent.balanceTrade();
                        if (tradeState.getCurrentState() == jelb.trade.State.States.Balanced) {
                            sendTradeAccept();
                        }
                    } else tradeEvent.setMeAccept(tradeEvent.getMineAcceptactionsCount() + 1);
                }
                return false;
            }
        });
    }

    public void createGame(GameSetup setup) {
        logger.info("Creating game");
        String gameName = setup.getGameName();
        File gameDir = new File(GAMES_DIRECTORY + File.separator + gameName);
        String hostname = setup.getHostName();
        String botName = setup.getBotName();
        String botClassName = setup.getBotClassName();
        String botMode = setup.getBotMode();
        String hostPassword = setup.getPassword();
        String identify = setup.getIdentify();
        int port = setup.getPort();
        Map<String, Object> setupProperties = setup.getProperties();
        try {
            GameBot bot = parseBotClass(botClassName);
            bot.setLogDirectory(LOG_DIRECTORY);
            bot.setBotName(botName);
            Game game = parseGame(gameDir, bot.getIrcCallback(), setupProperties);
            bot.setGame(game);
            if (!hostPassword.isEmpty()) bot.connectToHost(hostname, port, hostPassword); else bot.connectToHost(hostname, port);
            if (!identify.isEmpty()) bot.identify(identify);
            if (botMode != null && !botMode.isEmpty()) {
                logger.info("Setting mode " + botMode);
                bot.setMode(botMode);
            }
            for (Channel channel : setup.getChannels()) {
                String channelName = channel.getName();
                String channelPassword = channel.getPassword();
                if (!channelName.startsWith("#")) channelName = "#" + channelName;
                logger.info("Joining channel: " + channelName);
                if (channelPassword.isEmpty()) bot.connectToChannel(channelName); else bot.connectToChannel(channelName, channelPassword);
                String channelMode = channel.getMode();
                if (channelMode != null && !channelMode.isEmpty()) {
                    logger.info("Setting mode to " + channelMode);
                    bot.setMode(channelName, channelMode);
                }
                String topic = channel.getTopic();
                if (topic != null && !topic.isEmpty()) {
                    logger.info("Setting topic to '" + topic + "'");
                    bot.setTopic(channelName, topic);
                }
            }
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Error while creating game", e);
            e.printStackTrace();
        }
    }

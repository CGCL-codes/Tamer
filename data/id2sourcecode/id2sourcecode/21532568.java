    public void initializeServer() {
        logger.log(Level.INFO, "Initializing Rocket Server");
        logger.log(Level.CONFIG, "Creating {0} tables", new Object[] { NUM_TABLES });
        for (int i = 1; i <= NUM_TABLES; i++) {
            RocketTable rt = new RocketTable(i);
            tables.add(AppContext.getDataManager().createReference(rt));
        }
        logger.log(Level.CONFIG, "Tables created");
        logger.log(Level.CONFIG, "Creating server chat channel");
        Channel serverChatChannel = AppContext.getChannelManager().createChannel(RocketServerConstants.SERVER_CHAT_CHANNEL, this, Delivery.RELIABLE);
        serverChatChannelRef = AppContext.getDataManager().createReference(serverChatChannel);
        logger.log(Level.CONFIG, "Rocket Server initialization COMPLETE!");
    }

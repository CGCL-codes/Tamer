    public void getConnection(String userId, String password) throws InterruptedException {
        if (null == main) {
            logger.fatal("Must have a reference to Main in order to continue!");
            System.exit(1);
        }
        try {
            host = System.getProperty("HOST");
            port = Integer.parseInt(System.getProperty("PORT").trim());
            type = System.getProperty("TYPE");
            String authorizedUsersString = System.getProperty("AUTHORIZED_USERS");
            if (null != authorizedUsersString) authorizedUsers = authorizedUsersString.split(",");
            reconnectTimeout = Integer.parseInt(System.getProperty("RECONNECT_TIMEOUT").trim()) * 1000;
        } catch (Exception ex) {
            logger.fatal("Failed to load properties: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
        logger.info("Starting IM client");
        main.updateState(Main.State.Connected);
        ConnectionConfiguration connConfig = new ConnectionConfiguration(host, port, type);
        XMPPConnection connection = new XMPPConnection(connConfig);
        try {
            connection.connect();
            logger.info("Connected to " + connection.getHost());
        } catch (Exception ex) {
            logger.error("Failed to connect to " + connection.getHost() + "\n" + ex.getMessage());
            main.updateState(Main.State.Disconnected);
            return;
        }
        try {
            if (userId.contains("@")) userId = userId.substring(0, userId.indexOf("@"));
            connection.login(userId, password);
            logger.info("Logged in as " + connection.getUser());
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
        } catch (Exception ex) {
            logger.error("Failed to log in as " + connection.getUser() + "\n" + ex.getMessage());
            main.updateState(Main.State.Disconnected);
            return;
        }
        addPacketListener(connection);
        connections.put(userId, connection);
    }

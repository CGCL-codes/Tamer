    public void init(String username, String password) throws Exception {
        System.out.println(String.format("Initializing connection to server %1$s port %2$d", server, port));
        SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
        if (service == null) {
            config = new ConnectionConfiguration(server, port);
        } else {
            config = new ConnectionConfiguration(server, port, service);
        }
        if (server.equalsIgnoreCase("chat.facebook.com")) {
            System.out.println("SASL Enbaled!");
            SASLAuthentication.registerSASLMechanism("DIGEST-MD5", MySASLDigestMD5Mechanism.class);
            config = new ConnectionConfiguration("chat.facebook.com", 5222);
            config.setRosterLoadedAtLogin(true);
            config.setSASLAuthenticationEnabled(true);
        } else if (server.equalsIgnoreCase("gmail.com")) {
            config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
            config.setSASLAuthenticationEnabled(false);
        }
        if (port == -1) {
            connection = new XMPPConnection(server);
        } else {
            connection = new XMPPConnection(config);
        }
        connection.connect();
        if (!username.equals("")) {
            connection.login(username, password);
        }
        this.userName = username;
        if (server.equalsIgnoreCase("chat.facebook.com")) {
            setChatListener(new ChatListener(b, username + "@chat.facebook.com"));
        } else {
            setChatListener(new ChatListener(b, username));
        }
        getChatListener().init(connection);
        connection.getChatManager().addChatListener(getChatListener());
    }

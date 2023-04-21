    private void openConnection() throws XMPPException {
        ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        config.setSASLAuthenticationEnabled(true);
        config.setReconnectionAllowed(true);
        connection = new XMPPConnection(config);
        connection.connect();
        String userWithoutArroba = configuration.getUsername().substring(0, configuration.getUsername().lastIndexOf("@"));
        connection.login(userWithoutArroba, configuration.getPassword());
    }

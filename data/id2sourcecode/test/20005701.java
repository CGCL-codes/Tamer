    public boolean login() {
        XMPPConnection.DEBUG_ENABLED = isGuiDebug;
        ConnectionConfiguration connectionConfig = new ConnectionConfiguration(server, port, domain);
        connectionConfig.setReconnectionAllowed(true);
        connection = new XMPPConnection(connectionConfig);
        try {
            log.info("connecting ...");
            if (!connection.isConnected()) connection.connect();
            log.info("login ...");
            if (!connection.isAuthenticated()) connection.login(username, password);
            connection.addConnectionListener(conlistener);
            connection.addPacketListener(pktListener, pktFilter);
            if (connection.isAuthenticated()) {
                log.info("authenticated.");
            }
            log.info("ready to receive messages.");
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

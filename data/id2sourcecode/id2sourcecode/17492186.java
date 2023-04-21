    public void run() throws XMPPException {
        log.info("Establishing connection to server");
        connection.connect();
        log.info("Connected to server");
        connection.addConnectionListener(connectionListener);
        connection.addPacketListener(listener, new PacketTypeFilter(Message.class));
        connection.addPacketListener(presenceListener, new PacketTypeFilter(Presence.class));
        log.info("Performing authentication");
        connection.login(configuration.getUserName(), configuration.getPassword());
        log.info("Authenticated to server");
    }

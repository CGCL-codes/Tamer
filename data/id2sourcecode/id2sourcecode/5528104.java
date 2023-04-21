    protected void connectInternal(Properties props) throws IOException {
        boolean isUsessl = (Boolean) props.get("isUsessl");
        String host = props.getProperty("server");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        int port = Integer.parseInt(props.getProperty("port", isUsessl ? "5223" : "5222"));
        String resource = props.getProperty("resource", "JabberWeb");
        try {
            ConnectionConfiguration config = new ConnectionConfiguration(host, port);
            config.setCompressionEnabled(true);
            config.setSASLAuthenticationEnabled(true);
            if (isUsessl) {
                config.setSecurityMode(SecurityMode.enabled);
            }
            connection = new XMPPConnection(config);
            connection.connect();
            connection.login(username, password, resource);
            PacketFilter pf = new PacketTypeFilter(Presence.class);
            connection.addPacketListener(presenceListener, pf);
            connection.addPacketListener(groupParticipantListener, pf);
            connection.addConnectionListener(connectionListener);
            self = new Buddy(new JabberIdentifier(username, host, resource).toString());
            changeStatus(normalizeStatus(Presence.Type.available));
            reload();
            pingTimer = new Timer();
            pingTimer.schedule(pinger, 0, 240000);
        } catch (XMPPException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

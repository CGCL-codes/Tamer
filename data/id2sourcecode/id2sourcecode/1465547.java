    public void login() {
        Presence presence = new Presence(Presence.Type.available);
        presence.setMode(Presence.Mode.available);
        presence.setStatus("Gone fishing");
        try {
            SASLAuthentication.registerSASLMechanism("DIGEST-MD5", FBSASLDigestMD5Mechanism.class);
            ConnectionConfiguration config = new ConnectionConfiguration("chat.facebook.com", 5222);
            config.setRosterLoadedAtLogin(true);
            config.setDebuggerEnabled(true);
            config.setSASLAuthenticationEnabled(true);
            connection = new XMPPConnection(config);
            connection.connect();
            connection.login(username, password, "cronyim");
            connection.sendPacket(presence);
        } catch (XMPPException ex) {
            System.out.println(ex.getMessage());
            disconnect();
        }
    }

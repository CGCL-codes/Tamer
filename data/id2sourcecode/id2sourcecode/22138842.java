    public ConnectionManager(URI connectionUri, String chatHost) throws XMPPException, URISyntaxException {
        if (!"xmpp".equals(connectionUri.getScheme())) {
            throw new IllegalArgumentException("Invalid connection URI");
        }
        this.chatHost = chatHost;
        String[] userInfo = connectionUri.getUserInfo().split(":");
        if (userInfo.length != 2) {
            throw new IllegalArgumentException("Invalid connection URI");
        }
        connection = new XMPPConnection(connectionUri.getHost());
        connection.connect();
        connection.login(userInfo[0], userInfo[1]);
        jid = new URI("xmpp://" + connection.getUser());
        if (!jid.getUserInfo().equals(userInfo[0]) || !jid.getHost().equals(connectionUri.getHost())) {
            throw new XMPPException("Bad JID");
        }
    }

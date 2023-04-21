    public XMPPConnection connexion(String adress, String password, boolean status) {
        status = true;
        int arobas;
        String login, server;
        int port = 5222;
        arobas = adress.indexOf("@");
        if (arobas < 0) {
            status = false;
            return connexion;
        }
        login = adress.substring(0, arobas);
        server = adress.substring(arobas + 1);
        if (server == "ecole.fr") server = "127.0.0.1";
        config = new ConnectionConfiguration(server, port);
        connexion = new XMPPConnection(config);
        try {
            connexion.connect();
            connexion.login(login, password);
        } catch (XMPPException error) {
            status = false;
            connexion = null;
            return connexion;
        }
        Presence presence = new Presence(Presence.Type.available);
        connexion.sendPacket(presence);
        return connexion;
    }

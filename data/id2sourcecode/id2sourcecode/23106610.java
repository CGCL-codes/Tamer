    XMPPConnection connect(String id, String server, String user, String pass, String host, int port) {
        ConnectionConfiguration config;
        if (host == null) {
            config = new ConnectionConfiguration(server);
        } else {
            config = new ConnectionConfiguration(host, (port <= 0 ? 5269 : port), server);
        }
        XMPPConnection con = new XMPPConnection(config);
        try {
            con.connect();
            con.login(user, pass, "brazil" + id);
        } catch (XMPPException e) {
            System.out.println("Connect failure: " + e);
            return null;
        }
        return con;
    }

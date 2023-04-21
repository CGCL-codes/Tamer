    public XMPPConnection connect(String username, String password, String resource) {
        conn = new XMPPConnection(config);
        try {
            conn.connect();
            conn.login(username, password, resource);
            System.out.println("Login to Jabber Server: " + server + ":" + port + " as " + username + " was successfull.");
        } catch (XMPPException e) {
            System.err.println("Login to Jabber Server: " + server + ":" + port + " as " + username + " failed.");
            System.exit(1);
        }
        return conn;
    }

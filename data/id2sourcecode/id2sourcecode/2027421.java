    public void login(String userName, String password) throws XMPPException {
        ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        connection = new XMPPConnection(config);
        connection.connect();
        connection.login(userName, password, "AJIM");
    }

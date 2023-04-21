    public void login(String userName, String password) throws XMPPException {
        ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        connection = new XMPPConnection(config);
        connection.connect();
        System.out.println("ez meg megy 1");
        SASLAuthentication.supportSASLMechanism("PLAIN", 0);
        connection.login(userName, password, "proba");
        System.out.println(connection.isAuthenticated());
    }

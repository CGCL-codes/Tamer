    public static void connectAndLogin() throws XMPPException {
        connection = new JavverConnection();
        connection.connect();
        connection.login();
    }

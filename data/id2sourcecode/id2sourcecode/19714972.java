    public static void deleteUserAccoountOnServer(String serverAddress, String username, String defaultPassword) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(serverAddress);
        connection.connect();
        connection.login(username, defaultPassword);
        AccountManager manager = connection.getAccountManager();
        manager.deleteAccount();
        connection.disconnect();
    }

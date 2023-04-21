    public void deletAccount(String jabberServer, String jabberId, String jabberPassword) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(jabberServer);
        connection.connect();
        connection.login(jabberId, jabberPassword);
        AccountManager acm = connection.getAccountManager();
        acm.deleteAccount();
    }

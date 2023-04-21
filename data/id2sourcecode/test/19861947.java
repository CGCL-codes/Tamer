    public void login(String userName, String password) throws XMPPException {
        ConnectionConfiguration config = getProxyForXMPP(false);
        config.setCompressionEnabled(true);
        config.setSASLAuthenticationEnabled(true);
        SASLAuthentication.supportSASLMechanism("PLAIN", 0);
        connection = new XMPPConnection(config);
        connection.connect();
        connection.login(userName, password);
    }

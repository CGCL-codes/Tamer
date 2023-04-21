    public void connect() throws XMPPException {
        ConnectionConfiguration config = new ConnectionConfiguration(Constants.gtServer, Constants.gtPort, Constants.gtServiceName);
        config.setSASLAuthenticationEnabled(true);
        config.setReconnectionAllowed(true);
        con = new XMPPConnection(config);
        con.connect();
        con.login(Constants.gtUser, Constants.gtPass, serviceName);
        chat = new MultiUserChat(con, "p2p");
        setGenericListeners();
        setListeners();
        if (log.isTraceEnabled()) {
            log.trace("Login al GoogleTalk realizado correctamente con '" + Constants.gtUser + "'.");
        }
    }

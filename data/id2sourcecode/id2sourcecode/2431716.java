    public void connect() throws XMPPException {
        ConnectionConfiguration config = new ConnectionConfiguration(Constants.gtServer, Constants.gtPort, Constants.gtServiceName);
        config.setSASLAuthenticationEnabled(true);
        config.setReconnectionAllowed(true);
        con = new XMPPConnection(config);
        con.connect();
        con.login(Constants.gtUser, Constants.gtPass, "p2p");
        serviceListener = new ServiceManager();
        PacketFilter servFilter = new ThreadFilter(ServiceDefinition.class.getCanonicalName());
        con.createPacketCollector(servFilter);
        con.addPacketListener(serviceListener, servFilter);
        authListener = new AuthManager(this);
        PacketFilter[] filters = new PacketFilter[2];
        filters[0] = new ThreadFilter(AuthResponse.class.getCanonicalName());
        filters[1] = new NotFilter(new FromContainsFilter(getSMUserID()));
        PacketFilter filterf = new AndFilter(filters);
        con.createPacketCollector(filterf);
        con.addPacketListener(authListener, filterf);
        chat = new MultiUserChat(con, "p2p");
        manager = new FileTransferManager(con);
        if (log.isTraceEnabled()) {
            log.trace("Login al GoogleTalk realizado correctamente con '" + Constants.gtUser + "'.");
        }
    }

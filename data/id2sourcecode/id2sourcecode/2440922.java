    @PostConstruct
    public void init() {
        ConnectionConfiguration config = new ConnectionConfiguration(host, port, domain);
        config.setSASLAuthenticationEnabled(SASLAuthenticationEnabled);
        xmpp = new XMPPConnection(config);
        try {
            xmpp.connect();
            addLogOutShutdownHook();
            xmpp.login(username, password);
        } catch (XMPPException e) {
            log.error("Cannot connect to the XMPP server " + this.host + " with port " + this.port + " using account " + this.username + " in the domain " + this.domain, e);
            xmpp = null;
        }
    }

    public XmppRouter(XmppConfig config, JsonRpcService rpcService, boolean server) throws XMPPException {
        this.config = config;
        this.rpcService = rpcService;
        ConnectionConfiguration xmppConfig = new ConnectionConfiguration(config.getHost(), config.getPort());
        if (config.getProxy().getEnabled()) xmppConfig.setSocketFactory(new ProxySocketFactory(config.getProxy()));
        connection = new XMPPConnection(xmppConfig);
        PacketFilter filter = new AndFilter(new OrFilter(new MessageTypeFilter(Message.Type.chat), new MessageTypeFilter(Message.Type.normal)), new FromContainsFilter(config.getAcceptFrom()));
        connection.connect();
        collector = connection.createPacketCollector(filter);
        connection.login(config.getUser().getName(), config.getUser().getPassword(), config.getUser().getResource());
        from = connection.getUser();
        chatManager = connection.getChatManager();
        if (server) start();
    }

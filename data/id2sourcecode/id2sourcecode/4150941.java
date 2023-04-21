    protected void setUp(boolean clean) throws Exception {
        Properties props = SgsTestNode.getDefaultProperties(APP_NAME, null, DummyAppListener.class);
        this.protocolVersion = SimpleSgsProtocol.VERSION;
        props.setProperty(StandardProperties.AUTHENTICATORS, "com.sun.sgs.test.util.SimpleTestIdentityAuthenticator");
        props.setProperty("com.sun.sgs.impl.service.nodemap.policy.class", ConfigurableNodePolicy.class.getName());
        props.setProperty("com.sun.sgs.impl.protocol.simple.protocol.version", Byte.toString(protocolVersion));
        props.setProperty(StandardProperties.SESSION_RELOCATION_TIMEOUT_PROPERTY, "5000");
        serverNode = new SgsTestNode(APP_NAME, DummyAppListener.class, props, clean);
        port = serverNode.getAppPort();
        txnScheduler = serverNode.getSystemRegistry().getComponent(TransactionScheduler.class);
        taskOwner = serverNode.getProxy().getCurrentOwner();
        identityAssigner = new IdentityAssigner(serverNode);
        dataService = serverNode.getDataService();
        channelService = serverNode.getChannelService();
        wrapChannelServerProxy(serverNode);
    }

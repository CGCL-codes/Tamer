    public XmppRpcChannel(Executor threadPool, XmppAccount account) throws XMPPException {
        super(threadPool);
        setMaxMessageSize(40960);
        this.address = new XmppAddress(account.jid);
        ConnectionConfiguration connConfig = account.connectionConfig;
        xmppConnection = new XMPPConnection(connConfig);
        xmppConnection.connect();
        xmppConnection.login(account.name, account.passwd, Constants.PROJECT_NAME);
        Presence presence = new Presence(Presence.Type.available);
        xmppConnection.sendPacket(presence);
        super.start();
    }

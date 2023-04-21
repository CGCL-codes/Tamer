    public XMPPProxyConnection(GAEServerAuth auth, XmppAccount account) throws XMPPException {
        super(auth);
        this.address = new XmppAddress(account.jid);
        String appid = auth.backendEnable ? (GAEConstants.BACKEND_INSTANCE_NAME + "." + auth.appid) : auth.appid;
        this.serverAddress = new XmppAddress(appid + "@appspot.com");
        ConnectionConfiguration connConfig = account.connectionConfig;
        if (rawConnectionTable.containsKey(account.jid)) {
            xmppConnection = rawConnectionTable.get(account.jid);
        } else {
            xmppConnection = new XMPPConnection(connConfig);
            xmppConnection.connect();
            xmppConnection.login(account.name, account.passwd, GAEConstants.XMPP_CLIENT_NAME);
            Presence presence = new Presence(Presence.Type.available);
            xmppConnection.sendPacket(presence);
            rawConnectionTable.put(account.jid, xmppConnection);
        }
    }
